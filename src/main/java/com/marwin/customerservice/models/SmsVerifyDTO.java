package com.marwin.customerservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsVerifyDTO {
    private String verificationCode;
    private boolean isSent;
}
