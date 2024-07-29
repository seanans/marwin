package com.marwin.customerservice.models;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String phoneNumber;
    private String code;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
