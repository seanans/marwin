package com.marwin.customerservice.services;

import com.marwin.customerservice.entity.CustomerEntity;
import com.marwin.customerservice.exceptions.InputDataException;
import com.marwin.customerservice.mappers.CustomerMapper;
import com.marwin.customerservice.models.CreateCustomerDTO;
import com.marwin.customerservice.models.CustomerDTO;
import com.marwin.customerservice.models.CustomerResponse;
import com.marwin.customerservice.models.WelcomeResponse;
import com.marwin.customerservice.repository.CustomerRepository;
import com.marwin.customerservice.shared.ApiResponse;
import com.marwin.customerservice.visitor.CustomerRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private SmsService smsService;

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

    @Override
    public ResponseEntity<String> verifyPhoneNumber(String phoneNumber, String code) {
       try {
           if (!PhoneNumberIsValid.isValidPhoneNumber(phoneNumber)) {
               throw new InputDataException("Your phone number is incorrect");
           }
           var customer = customerRepository.getCustomerEntityByPhoneNumber(phoneNumber);
           if (customer.isVerifiedPhoneNumber()) {
               throw new InputDataException("Your phone is already verified");
           }
           if(customer.getVerificationCode().equals(code)) {
               customer.setVerifiedPhoneNumber(true);
               customerRepository.save(customer);
               return  new ResponseEntity<>("Your account was verified by phone number", HttpStatus.OK);
           } else
               throw new InputDataException("Your code is incorrect");
       } catch (InputDataException e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }

    }


    @Override
    public List<CustomerDTO> searchByRsql(String rsqlQuery) {
        Node rootNode = new RSQLParser().parse(rsqlQuery);
        Specification<CustomerEntity> specification = rootNode.accept(new CustomerRsqlVisitor<CustomerEntity>());
        System.out.println("Service Impl!");
        return customerMapper.customerEntitiesToCustomerDTO(customerRepository.findAll(specification));
    }

    @Override
    public Boolean sendSms(String phoneNumber) {
            if (!PhoneNumberIsValid.isValidPhoneNumber(phoneNumber)) {
                throw new InputDataException("Your phone number is incorrect");
            }
            var customer = customerRepository.getCustomerEntityByPhoneNumber(phoneNumber);
            if (customer.isVerifiedPhoneNumber()) {
                throw new InputDataException("Your phone number is already verified");
            } else {
                var smsStatus = smsService.sendSms(phoneNumber);
                if (smsStatus.isSent()) {

                    customer.setVerificationCode(smsStatus.getVerificationCode());
                    customerRepository.save(customer);
                    return true;
                } else
                    return false;
            }
    }


}
