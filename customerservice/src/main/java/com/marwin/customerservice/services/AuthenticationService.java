package com.marwin.customerservice.services;

import com.marwin.customerservice.models.JwtResponse;
import com.marwin.customerservice.models.LoginRequest;

public interface AuthenticationService {
    JwtResponse verifyAndAuthenticate(String phoneNumber, String code);
}
