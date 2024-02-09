package com.marwin.customerservice.models;

import lombok.Data;


@Data
public class CustomerDTO {
    private String phoneNumber;
    private String name;
    private String email;
    private  Integer balance;
}
