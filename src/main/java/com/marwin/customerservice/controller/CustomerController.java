package com.marwin.customerservice.controller;

import com.marwin.customerservice.models.CreateCustomerDTO;
import com.marwin.customerservice.models.CustomerDTO;
import com.marwin.customerservice.models.WelcomeResponse;
import com.marwin.customerservice.services.CustomerService;
import com.marwin.customerservice.shared.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/customer")

public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @PostMapping("/create")
    //private ResponseEntity<ApiResponse<WelcomeResponse>> createCustomer(@RequestBody CreateCustomerDTO customerDTO) {
    //return customerService.createCustomer(customerDTO);
    private ResponseEntity<?> createCustomer(@RequestBody CreateCustomerDTO customerDTO) {
        ApiResponse<WelcomeResponse> response = customerService.createCustomer(customerDTO).getBody();

        assert response != null;
        if (response.isSuccess()) {
            return new ResponseEntity<>(response.getData(), HttpStatus.CREATED);
        } else if (response.hasError()) {
            return new ResponseEntity<>(response.getErrorMessage(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/customers")
    @ResponseBody
    private List<CustomerDTO> findAllByRsql(@RequestParam(value = "search") String search) {
        System.out.println("Controller!");
        return customerService.searchByRsql(search);
    }

    @GetMapping("/test")
    private void test() {
        System.out.println("Here!");
    }


}
