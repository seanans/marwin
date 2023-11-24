package com.marwin.customerservice.controller;

import com.marwin.customerservice.models.CreateCustomerDTO;
import com.marwin.customerservice.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    private HttpStatus createUser(@RequestBody CreateCustomerDTO customerDTO) {
        customerService.createUser(customerDTO);
        return HttpStatus.CREATED;
    }


}
