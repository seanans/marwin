package com.marwin.customerservice.services;

import com.marwin.customerservice.models.CustomerDTO;
import com.marwin.customerservice.models.UpdateProfileDTO;

import java.util.List;


public interface CustomerService {
    void createCustomerWithPhoneNumber(String phoneNumber);

    List<CustomerDTO> searchByRsql(String rsqlQuery);

    void sendSms(String phoneNumber);

    void verifyPhoneNumber(String phoneNumber, String code);

    void addToBalance(String phoneNumber, Integer amount);

    boolean customerExists(String phoneNumber);

    void addNameToProfile(String phoneNumber, String name);

    void updateProfileInfo(String phoneNumber, UpdateProfileDTO profileDTO);
}
