package com.marwin.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }) //disable security
@ComponentScan(basePackages = {"com.marwin.customerservice", "com.marwin.customerservice.mappers", "com.marwin.customerservice.controller"})
public class CustomerserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerserviceApplication.class, args);
    }

}
