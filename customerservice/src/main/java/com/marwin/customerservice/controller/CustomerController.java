package com.marwin.customerservice.controller;

import com.marwin.customerservice.exceptions.InputDataException;
import com.marwin.customerservice.models.CreateCustomerDTO;
import com.marwin.customerservice.models.CustomerDTO;
import com.marwin.customerservice.services.CustomerService;
import com.marwin.customerservice.services.SmsService;
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

    @Autowired
    private SmsService smsService;

    @PostMapping("/create")
    private ResponseEntity<?> createCustomer(@RequestBody CreateCustomerDTO customerDTO) {
        try {
            customerService.createCustomer(customerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InputDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/find")
    @ResponseBody
    private List<CustomerDTO> findAllByRsql(@RequestParam(value = "search") String search) {
        System.out.println("Controller!");
        return customerService.searchByRsql(search);
    }

    @PostMapping("/verify")
    private ResponseEntity<?> sendVerificationCode(@RequestParam("phoneNumber") String phoneNumber) {
        try {
            customerService.sendSms(phoneNumber);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InputDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/verify/confirm")
    private ResponseEntity<?> verifyAccount(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("code") String code) {
        try {
            customerService.verifyPhoneNumber(phoneNumber, code);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (InputDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/test")
    private void test() {
        System.out.println("Here!");
    }


}
