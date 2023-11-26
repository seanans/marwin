package com.marwin.customerservice.services;

import com.marwin.customerservice.exceptions.InputDataException;
import com.marwin.customerservice.mappers.CustomerMapper;
import com.marwin.customerservice.models.CreateCustomerDTO;
import com.marwin.customerservice.models.CustomerResponse;
import com.marwin.customerservice.models.WelcomeResponse;
import com.marwin.customerservice.repository.CustomerRepository;
import com.marwin.customerservice.shared.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public ResponseEntity<ApiResponse<WelcomeResponse>> createCustomer(CreateCustomerDTO customerDTO) {

        try {
            if (!PhoneNumberIsValid.isValidPhoneNumber(customerDTO.getPhoneNumber())) {
                throw new InputDataException("Your phone number is incorrect");
            }
            if (customerRepository.existsByPhoneNumber(customerDTO.getPhoneNumber())) {
                throw new InputDataException("Your phone number is already in use");
            }
            if (!NameIsValid.isValidName(customerDTO.getName())) {
                throw new InputDataException("Your name is incorrect");
            }
            if (!EmailValid.isValidEmail(customerDTO.getEmail())) {
                throw new InputDataException("Your email is incorrect");
            }
            if (customerRepository.existsByEmail(customerDTO.getEmail())) {
                throw new InputDataException("Your email is already in use");
            }

            customerRepository.save(customerMapper.createCustomerDTOToCustomerEntity(customerDTO));
            String welcomeMessage = String.format("Welcome, %s! Your account has been successfully created.",
                    customerDTO.getName());
            CustomerResponse customerResponse = new CustomerResponse(customerDTO.getPhoneNumber(), customerDTO.getName(), customerDTO.getEmail());
            WelcomeResponse welcomeResponse = new WelcomeResponse(welcomeMessage, customerResponse);
            return new ResponseEntity<>(ApiResponse.success(welcomeResponse), HttpStatus.CREATED);
        } catch (InputDataException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }
}
