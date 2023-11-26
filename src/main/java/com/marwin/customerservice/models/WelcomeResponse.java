package com.marwin.customerservice.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WelcomeResponse {
    private String messageResponse;
    private CustomerResponse customerResponse;
}
