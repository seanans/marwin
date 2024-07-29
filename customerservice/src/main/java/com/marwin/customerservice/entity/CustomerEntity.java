package com.marwin.customerservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity
@Data
public class CustomerEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "customer_id", nullable = false)
    private UUID id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    @Pattern(regexp = "^\\+\\d+$", message = "Phone number must start with a plus sign and contain only digits.")
    private String phoneNumber;

    @Column(unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @Column
    private String name;

    @Column(columnDefinition = "BOOLEAN default false")
    private boolean isVerifiedPhoneNumber;

    @Column
    private String verificationCode;

    @Column(nullable = false)
    private Integer balance = 0;
    public CustomerEntity() {

    }
}
