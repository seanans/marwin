package com.marwin.customerservice.controllers;

import com.marwin.customerservice.exceptions.InputDataException;
import com.marwin.customerservice.models.JwtResponse;
import com.marwin.customerservice.services.AuthenticationService;
import com.marwin.customerservice.services.CustomerService;
import com.marwin.customerservice.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitPhoneNumber_whenPhoneNumberExists_thenSendSms() {
        String phoneNumber = "+380976727479";

        when(customerService.customerExists(anyString())).thenReturn(true);
        doNothing().when(customerService).sendSms(anyString());

        ResponseEntity<?> response = customerController.submitPhoneNumber(phoneNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerService, times(1)).sendSms(phoneNumber);
    }

    @Test
    void submitPhoneNumber_whenPhoneNumberDoesNotExist_thenCreateCustomerAndSendSms() {
        String phoneNumber = "+380976727479";

        when(customerService.customerExists(anyString())).thenReturn(false);
        doNothing().when(customerService).createCustomerWithPhoneNumber(anyString());

        ResponseEntity<?> response = customerController.submitPhoneNumber(phoneNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerService, times(1)).createCustomerWithPhoneNumber(phoneNumber);
    }

    @Test
    void verifyAccount_whenCodeIsValid_thenReturnJwtToken() {
        String phoneNumber = "+380976727479";
        String code = "123456";
        String jwtToken = "mockJwtToken";

        doNothing().when(customerService).verifyPhoneNumber(anyString(), anyString());
        when(authenticationService.authenticateUser(anyString())).thenReturn(new JwtResponse(jwtToken));

        ResponseEntity<?> response = customerController.verifyAccount(phoneNumber, code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse body = (JwtResponse) response.getBody();
        assertNotNull(body);
        assertEquals(jwtToken, body.jwt());
    }

    @Test
    void verifyAccount_whenCodeIsInvalid_thenReturnUnauthorized() {
        String phoneNumber = "+380976727479";
        String code = "123456";

        doThrow(InputDataException.class).when(customerService).verifyPhoneNumber(anyString(), anyString());

        ResponseEntity<?> response = customerController.verifyAccount(phoneNumber, code);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void validateToken_whenTokenIsValid_thenReturnJwtResponse() {
        String token = "mockJwtToken";
        String phoneNumber = "+380976727479";

        Claims claims = Jwts.claims().setSubject(phoneNumber);
        when(jwtUtil.extractClaims(anyString())).thenReturn(claims);
        when(jwtUtil.validateToken(anyString(), anyString())).thenReturn(true);

        ResponseEntity<?> response = customerController.validateToken(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse body = (JwtResponse) response.getBody();
        assertNotNull(body);
        assertEquals(token, body.jwt());
    }

    @Test
    void validateToken_whenTokenIsInvalid_thenReturnUnauthorized() {
        String token = "mockJwtToken";

        when(jwtUtil.extractClaims(anyString())).thenThrow(RuntimeException.class);

        ResponseEntity<?> response = customerController.validateToken(token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
