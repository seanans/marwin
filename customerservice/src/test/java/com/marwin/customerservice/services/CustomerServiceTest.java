package com.marwin.customerservice.services;

import com.marwin.customerservice.entity.CustomerEntity;
import com.marwin.customerservice.exceptions.InputDataException;
import com.marwin.customerservice.mappers.CustomerMapper;
import com.marwin.customerservice.models.CustomerDTO;
import com.marwin.customerservice.models.SmsVerifyDTO;
import com.marwin.customerservice.models.UpdateProfileDTO;
import com.marwin.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

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
    @Mock
    private CustomerMapper customerMapper;

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

    @Test
    void addNameToProfile_whenNameIsValid_thenSuccess() {
        String phoneNumber = "+380976727479";
        String name = "John Doe";

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber(phoneNumber);

        when(customerRepository.getCustomerEntityByPhoneNumber(phoneNumber)).thenReturn(customerEntity);

        assertDoesNotThrow(() -> customerService.addNameToProfile(phoneNumber, name));
        verify(customerRepository, times(1)).save(customerEntity);
        assertEquals(name, customerEntity.getName());
        assertTrue(customerEntity.isProfileComplete());
    }

    @Test
    void addNameToProfile_whenNameIsInvalid_thenThrowsException() {
        String phoneNumber = "+380976727479";
        String name = "";

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber(phoneNumber);

        when(customerRepository.getCustomerEntityByPhoneNumber(phoneNumber)).thenReturn(customerEntity);

        assertThrows(InputDataException.class, () -> customerService.addNameToProfile(phoneNumber, name));
    }

    @Test
    void updateProfileInfo_whenAllFieldsAreUpdated_thenSuccess() {
        String phoneNumber = "+380976727479";
        UpdateProfileDTO profileDTO = new UpdateProfileDTO();
        profileDTO.setName("John Doe");
        profileDTO.setEmail("johndoe@example.com");
        profileDTO.setProfilePhoto("http://example.com/photos/johndoe.jpg");
        profileDTO.setHomeAddress("123 Elm St");
        profileDTO.setPreferredPaymentMethod("Credit Card");
        profileDTO.setEmergencyContact("+380123456789");
        profileDTO.setPreferredLanguage("English");

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber(phoneNumber);

        when(customerRepository.getCustomerEntityByPhoneNumber(phoneNumber)).thenReturn(customerEntity);

        assertDoesNotThrow(() -> customerService.updateProfileInfo(phoneNumber, profileDTO));

        verify(customerRepository, times(1)).save(customerEntity);
        assertEquals("John Doe", customerEntity.getName());
        assertEquals("johndoe@example.com", customerEntity.getEmail());
        assertEquals("http://example.com/photos/johndoe.jpg", customerEntity.getProfilePhoto());
        assertEquals("123 Elm St", customerEntity.getHomeAddress());
        assertEquals("Credit Card", customerEntity.getPreferredPaymentMethod());
        assertEquals("+380123456789", customerEntity.getEmergencyContact());
        assertEquals("English", customerEntity.getPreferredLanguage());
        assertTrue(customerEntity.isProfileComplete());
    }

    @Test
    void updateProfileInfo_whenOnlySomeFieldsAreUpdated_thenSuccess() {
        String phoneNumber = "+380976727479";
        UpdateProfileDTO profileDTO = new UpdateProfileDTO();
        profileDTO.setName("John Doe");
        profileDTO.setEmail(null);  // Email is not provided
        profileDTO.setHomeAddress("123 Elm St");

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber(phoneNumber);

        when(customerRepository.getCustomerEntityByPhoneNumber(phoneNumber)).thenReturn(customerEntity);

        assertDoesNotThrow(() -> customerService.updateProfileInfo(phoneNumber, profileDTO));

        verify(customerRepository, times(1)).save(customerEntity);
        assertEquals("John Doe", customerEntity.getName());
        assertEquals("123 Elm St", customerEntity.getHomeAddress());
        assertNull(customerEntity.getEmail());  // Email should remain unchanged
    }

    @Test
    void sendSms_whenPhoneNumberIsValid_thenSmsSentSuccessfully() {
        String phoneNumber = "+380976727479";

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber(phoneNumber);
        customerEntity.setVerifiedPhoneNumber(false);

        when(customerRepository.getCustomerEntityByPhoneNumber(phoneNumber)).thenReturn(customerEntity);
        when(smsService.sendSms(anyString())).thenReturn(new SmsVerifyDTO("123456", true));

        assertDoesNotThrow(() -> customerService.sendSms(phoneNumber));
        verify(customerRepository, times(1)).save(customerEntity);
        assertEquals("123456", customerEntity.getVerificationCode());
    }

    @Test
    void sendSms_whenPhoneNumberIsAlreadyVerified_thenThrowsException() {
        String phoneNumber = "+380976727479";

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber(phoneNumber);
        customerEntity.setVerifiedPhoneNumber(true);

        when(customerRepository.getCustomerEntityByPhoneNumber(phoneNumber)).thenReturn(customerEntity);

        assertThrows(InputDataException.class, () -> customerService.sendSms(phoneNumber));
    }

    @Test
    void searchByRsql_whenQueryIsValid_thenReturnsResults() {
        String rsqlQuery = "phoneNumber=='+380976727479'";

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPhoneNumber("+380976727479");

        when(customerRepository.findAll(any(Specification.class))).thenReturn(List.of(customerEntity));
        when(customerMapper.customerEntitiesToCustomerDTO(anyList())).thenReturn(List.of(new CustomerDTO()));

        List<CustomerDTO> results = customerService.searchByRsql(rsqlQuery);

        assertNotNull(results);
        assertFalse(results.isEmpty());
    }
}
