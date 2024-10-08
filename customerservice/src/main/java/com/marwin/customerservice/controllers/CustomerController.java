package com.marwin.customerservice.controllers;

import com.marwin.customerservice.exceptions.InputDataException;
import com.marwin.customerservice.models.CustomerDTO;
import com.marwin.customerservice.models.JwtResponse;
import com.marwin.customerservice.models.UpdateProfileDTO;
import com.marwin.customerservice.services.AuthenticationService;
import com.marwin.customerservice.services.CustomerService;
import com.marwin.customerservice.utils.JwtUtil;
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
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/submit-phone")
    public ResponseEntity<?> submitPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        try {
            if (customerService.customerExists(phoneNumber)) {
                customerService.sendSms(phoneNumber);
            } else {
                customerService.createCustomerWithPhoneNumber(phoneNumber);
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (InputDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/find")
    @ResponseBody
    private List<CustomerDTO> findAllByRsql(@RequestParam(value = "search") String search) {
        return customerService.searchByRsql(search);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("code") String code) {
        try {
            customerService.verifyPhoneNumber(phoneNumber, code);
            JwtResponse jwtResponse = authenticationService.authenticateUser(phoneNumber);
            return ResponseEntity.ok(jwtResponse);
        } catch (InputDataException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid verification code");
        }
    }

    @PatchMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token, @RequestBody UpdateProfileDTO profileDTO) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove the "Bearer " part
            }
            
            String phoneNumber = jwtUtil.extractClaims(token).getSubject();
            customerService.updateProfileInfo(phoneNumber, profileDTO);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (InputDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/add-name")
    public ResponseEntity<?> addName(@RequestHeader("Authorization") String token, @RequestParam("name") String name) {
        try {

            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove the "Bearer " part
            }

            String phoneNumber = jwtUtil.extractClaims(token).getSubject();

            customerService.addNameToProfile(phoneNumber, name);
            return ResponseEntity.ok("Name added successfully");
        } catch (InputDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
        try {
            String phoneNumber = jwtUtil.extractClaims(token).getSubject();
            if (jwtUtil.validateToken(token, phoneNumber)) {
                return ResponseEntity.ok(new JwtResponse(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    //Need official documents to integrate with real payments.
    @PostMapping("/balance/add")
    private ResponseEntity<?> addToBalance(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("amount") Integer amount) {
        try {
            customerService.addToBalance(phoneNumber, amount);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (InputDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/test")
    private void test() {
        System.out.println("Here!");
    }


}
