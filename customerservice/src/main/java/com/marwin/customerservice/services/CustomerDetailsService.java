package com.marwin.customerservice.services;

import com.marwin.customerservice.entity.CustomerEntity;
import com.marwin.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        CustomerEntity customer = customerRepository.getCustomerEntityByPhoneNumber(phoneNumber);
        if (customer == null) {
            throw new UsernameNotFoundException("User not found with phone number: " + phoneNumber);
        }
        return new org.springframework.security.core.userdetails.User(customer.getPhoneNumber(), "", Collections.emptyList());
    }
}
