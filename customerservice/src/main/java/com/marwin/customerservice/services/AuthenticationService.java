package com.marwin.customerservice.services;

import com.marwin.customerservice.models.JwtResponse;

public interface AuthenticationService {
    JwtResponse verifyAndAuthenticate(String phoneNumber, String code);
}
