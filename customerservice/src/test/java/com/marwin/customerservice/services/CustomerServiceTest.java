package com.marwin.customerservice.services;

import com.marwin.customerservice.entity.CustomerEntity;
import com.marwin.customerservice.exceptions.InputDataException;
import com.marwin.customerservice.models.SmsVerifyDTO;
import com.marwin.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private SmsService smsService;
    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomerWithPhoneNumber_whenPhoneNumberIsValid_thenSuccess() {
        String phoneNumber = "+380976727479";

        when(customerRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(new CustomerEntity());
        when(smsService.sendSms(anyString())).thenReturn(new SmsVerifyDTO("123456", true));

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber(phoneNumber);
        when(customerRepository.getCustomerEntityByPhoneNumber(phoneNumber)).thenReturn(customerEntity);

        assertDoesNotThrow(() -> customerService.createCustomerWithPhoneNumber(phoneNumber));
        verify(smsService, times(1)).sendSms(phoneNumber);
    }

    @Test
    void createCustomerWithPhoneNumber_whenPhoneNumberIsInvalid_thenThrowsException() {
        String phoneNumber = "+1234567890";

        assertThrows(InputDataException.class, () -> customerService.createCustomerWithPhoneNumber(phoneNumber));
    }

    @Test
    void createCustomerWithPhoneNumber_whenPhoneNumberExists_thenThrowsException() {
        String phoneNumber = "+380976727479";

        when(customerRepository.existsByPhoneNumber(anyString())).thenReturn(true);

        assertThrows(InputDataException.class, () -> customerService.createCustomerWithPhoneNumber(phoneNumber));
    }

    @Test
    void verifyPhoneNumber_whenCodeIsValid_thenSuccess() {
        String phoneNumber = "+380976727479";
        String code = "123456";

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber(phoneNumber);
        customerEntity.setVerificationCode(code);
        customerEntity.setVerifiedPhoneNumber(false);

        when(customerRepository.getCustomerEntityByPhoneNumber(anyString())).thenReturn(customerEntity);
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);

        assertDoesNotThrow(() -> customerService.verifyPhoneNumber(phoneNumber, code));
        assertTrue(customerEntity.isVerifiedPhoneNumber());
    }

    @Test
    void verifyPhoneNumber_whenCodeIsInvalid_thenThrowsException() {
        String phoneNumber = "+380976727479";
        String code = "123456";

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber(phoneNumber);
        customerEntity.setVerificationCode("654321");
        customerEntity.setVerifiedPhoneNumber(false);

        when(customerRepository.getCustomerEntityByPhoneNumber(anyString())).thenReturn(customerEntity);

        assertThrows(InputDataException.class, () -> customerService.verifyPhoneNumber(phoneNumber, code));
    }

    @Test
    void customerExists_whenPhoneNumberExists_thenReturnsTrue() {
        String phoneNumber = "+380976727479";

        when(customerRepository.existsByPhoneNumber(anyString())).thenReturn(true);

        assertTrue(customerService.customerExists(phoneNumber));
    }

    @Test
    void addToBalance_whenPhoneIsVerifiedAndAmountIsValid_thenSuccess() {
        String phoneNumber = "+380976727479";
        int amount = 100;

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber(phoneNumber);
        customerEntity.setBalance(0);
        customerEntity.setVerifiedPhoneNumber(true);

        when(customerRepository.getCustomerEntityByPhoneNumber(anyString())).thenReturn(customerEntity);
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);

        assertDoesNotThrow(() -> customerService.addToBalance(phoneNumber, amount));
        assertEquals(100, customerEntity.getBalance());
    }

    @Test
    void addToBalance_whenPhoneIsNotVerified_thenThrowsException() {
        String phoneNumber = "+380976727479";
        int amount = 100;

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber(phoneNumber);
        customerEntity.setBalance(0);
        customerEntity.setVerifiedPhoneNumber(false);

        when(customerRepository.getCustomerEntityByPhoneNumber(anyString())).thenReturn(customerEntity);

        assertThrows(InputDataException.class, () -> customerService.addToBalance(phoneNumber, amount));
    }
}
