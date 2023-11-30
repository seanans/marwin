package com.marwin.customerservice.services;

import com.marwin.customerservice.models.CreateCustomerDTO;
import com.marwin.customerservice.models.CustomerDTO;
import com.marwin.customerservice.models.WelcomeResponse;
import com.marwin.customerservice.shared.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface CustomerService {
    ResponseEntity<ApiResponse<WelcomeResponse>> createCustomer (CreateCustomerDTO customerDTO);
    List<CustomerDTO> searchByRsql(String rsqlQuery);
}
