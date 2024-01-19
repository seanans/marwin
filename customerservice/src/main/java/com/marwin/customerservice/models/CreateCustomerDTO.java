package com.marwin.customerservice.models;

import lombok.Data;


@Data
public class CreateCustomerDTO {
    private String phoneNumber;
    private String name;
    private String email;
}
