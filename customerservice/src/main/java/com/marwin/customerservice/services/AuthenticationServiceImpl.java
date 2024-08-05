package com.marwin.customerservice.services;

import com.marwin.customerservice.models.JwtResponse;
import com.marwin.customerservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerService customerService;

    @Override
    public JwtResponse authenticateUser(String phoneNumber) {
        final UserDetails userDetails = customerDetailsService.loadUserByUsername(phoneNumber);
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return new JwtResponse(jwt);
    }

    @Override
    public void verifyPhoneNumberCode(String phoneNumber, String code) {
        customerService.verifyPhoneNumber(phoneNumber, code);
    }
}
