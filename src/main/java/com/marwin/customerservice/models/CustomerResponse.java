package com.marwin.customerservice.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private String phoneNumber;
    private String name;
    private String email;
}
