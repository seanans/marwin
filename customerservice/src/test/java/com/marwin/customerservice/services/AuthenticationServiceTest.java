package com.marwin.customerservice.services;

import com.marwin.customerservice.exceptions.InputDataException;
import com.marwin.customerservice.models.JwtResponse;
import com.marwin.customerservice.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    @Mock
    private CustomerDetailsService customerDetailsService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void verifyPhoneNumberCode_whenCodeIsValid_thenSuccess() {
        String phoneNumber = "+380976727479";
        String code = "123456";

        doNothing().when(customerService).verifyPhoneNumber(phoneNumber, code);

        assertDoesNotThrow(() -> authenticationService.verifyPhoneNumberCode(phoneNumber, code));
        verify(customerService, times(1)).verifyPhoneNumber(phoneNumber, code);
    }

    @Test
    void verifyPhoneNumberCode_whenCodeIsInvalid_thenThrowsException() {
        String phoneNumber = "+380976727479";
        String code = "123456";

        doThrow(InputDataException.class).when(customerService).verifyPhoneNumber(phoneNumber, code);

        assertThrows(InputDataException.class, () -> authenticationService.verifyPhoneNumberCode(phoneNumber, code));
    }

    @Test
    void authenticateUser_whenPhoneNumberIsValid_thenSuccess() {
        String phoneNumber = "+380976727479";
        String jwtToken = "mockJwtToken";

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(phoneNumber);

        when(customerDetailsService.loadUserByUsername(phoneNumber)).thenReturn(userDetails);
        when(jwtUtil.generateToken(phoneNumber)).thenReturn(jwtToken);

        JwtResponse jwtResponse = authenticationService.authenticateUser(phoneNumber);

        assertNotNull(jwtResponse);
        assertEquals(jwtToken, jwtResponse.jwt());
    }

    @Test
    void authenticateUser_whenPhoneNumberIsInvalid_thenThrowsException() {
        String phoneNumber = "+380976727479";

        when(customerDetailsService.loadUserByUsername(phoneNumber)).thenThrow(new InputDataException("User not found"));

        assertThrows(InputDataException.class, () -> authenticationService.authenticateUser(phoneNumber));
    }
}
