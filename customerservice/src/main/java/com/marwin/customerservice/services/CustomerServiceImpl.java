package com.marwin.customerservice.services;

import com.marwin.customerservice.entity.CustomerEntity;
import com.marwin.customerservice.exceptions.InputDataException;
import com.marwin.customerservice.mappers.CustomerMapper;
import com.marwin.customerservice.models.CreateCustomerDTO;
import com.marwin.customerservice.models.CustomerDTO;
import com.marwin.customerservice.repository.CustomerRepository;
import com.marwin.customerservice.visitor.CustomerRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
    public void createCustomerWithPhoneNumber(String phoneNumber) {
        if (!PhoneNumberIsValid.isValidPhoneNumber(phoneNumber)) {
            throw new InputDataException("Your phone number is incorrect");
        }
        if (customerRepository.existsByPhoneNumber(phoneNumber)) {
            throw new InputDataException("Your phone number is already in use");
        }
        var customer = new CustomerEntity();
        customer.setPhoneNumber(phoneNumber);
        customerRepository.save(customer);
        sendSms(phoneNumber);
    }
    /*
    @Override
    public void createCustomer(CreateCustomerDTO customerDTO) {
        validateCustomerData(customerDTO);
        customerRepository.save(customerMapper.createCustomerDTOToCustomerEntity(customerDTO));
    }

    private void validateCustomerData(CreateCustomerDTO customerDTO) {
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
    }
*/
    @Override
    public void verifyPhoneNumber(String phoneNumber, String code) {
        if (!PhoneNumberIsValid.isValidPhoneNumber(phoneNumber)) {
            throw new InputDataException("Invalid phone number");
        }
        var customer = customerRepository.getCustomerEntityByPhoneNumber(phoneNumber);
        if (customer.isVerifiedPhoneNumber()) {
            throw new InputDataException("Phone already verified");
        }
        if (customer.getVerificationCode().equals(code)) {
            customer.setVerifiedPhoneNumber(true);
            customerRepository.save(customer);
        } else
            throw new InputDataException("Incorrect code");
    }

    @Override
    public void addToBalance(String phoneNumber, Integer amount) {
        if (!PhoneNumberIsValid.isValidPhoneNumber(phoneNumber)) {
            throw new InputDataException("Invalid phone number");
        }
        if (amount == 0 || amount < 0) {
            throw new InputDataException("You can`t add nothing to your balance");
        }
        var customer = customerRepository.getCustomerEntityByPhoneNumber(phoneNumber);
        if (!customer.isVerifiedPhoneNumber()) {
            throw new InputDataException("Phone does not verified, you can`t add to your account without phone verification");
        }
        customer.setBalance(customer.getBalance() + amount);
        customerRepository.save(customer);
    }

    @Override
    public boolean customerExists(String phoneNumber) {
        return customerRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public List<CustomerDTO> searchByRsql(String rsqlQuery) {
        Node rootNode = new RSQLParser().parse(rsqlQuery);
        Specification<CustomerEntity> specification = rootNode.accept(new CustomerRsqlVisitor<>());
        System.out.println("Service Impl!");
        return customerMapper.customerEntitiesToCustomerDTO(customerRepository.findAll(specification));
    }

    @Override
    public void sendSms(String phoneNumber) {
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
            } else
                throw new RuntimeException("SMS sending failed");
        }
    }


}
