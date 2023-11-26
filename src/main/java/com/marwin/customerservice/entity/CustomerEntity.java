package com.marwin.customerservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.hibernate.annotations.GenericGenerator;

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

    @NotNull
    @NotEmpty
    @Column(unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @NotNull
    @NotEmpty
    private String name;

    public CustomerEntity() {

    }
}
