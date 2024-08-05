package com.marwin.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }) //disable security
@SpringBootApplication
@ComponentScan(basePackages = {"com.marwin.customerservice", "com.marwin.customerservice.mappers", "com.marwin.customerservice.controllers"})
public class CustomerserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerserviceApplication.class, args);
    }

}
