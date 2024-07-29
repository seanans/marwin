package com.marwin.customerservice.services;

import com.marwin.customerservice.models.JwtResponse;
import com.marwin.customerservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerService customerService;

    @Override
    public JwtResponse verifyAndAuthenticate(String phoneNumber, String code) {
        customerService.verifyPhoneNumber(phoneNumber, code);

        final UserDetails userDetails = customerDetailsService.loadUserByUsername(phoneNumber);
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new JwtResponse(jwt);
    }
}
