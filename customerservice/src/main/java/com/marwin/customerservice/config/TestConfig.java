package com.marwin.customerservice.config;

import com.marwin.customerservice.models.SmsVerifyDTO;
import com.marwin.customerservice.services.SmsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.*;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    public SmsService smsService() {
        SmsService mockSmsService = mock(SmsService.class);
        when(mockSmsService.sendSms(anyString())).thenReturn(new SmsVerifyDTO("123456", true));
        return mockSmsService;
    }
}
