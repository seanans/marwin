package com.marwin.customerservice.services;

import com.marwin.customerservice.models.CreateCustomerDTO;
import com.marwin.customerservice.models.CustomerDTO;

import java.util.List;


public interface CustomerService {
    void createCustomer (CreateCustomerDTO customerDTO);
    List<CustomerDTO> searchByRsql(String rsqlQuery);

    void sendSms(String phoneNumber);

    void verifyPhoneNumber (String phoneNumber, String code);

}
