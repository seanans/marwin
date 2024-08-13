package com.marwin.customerservice.models;

import lombok.Data;

@Data
public class UpdateProfileDTO {
    private String name;
    private String email;
    private String profilePhoto;
    private String homeAddress;
    private String preferredPaymentMethod;
    private String emergencyContact;
    private String preferredLanguage;
}
