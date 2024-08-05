package com.marwin.customerservice.services;

import com.marwin.customerservice.models.JwtResponse;

public interface AuthenticationService {
    JwtResponse authenticateUser(String phoneNumber);

    void verifyPhoneNumberCode(String phoneNumber, String code);
}
