package com.marwin.customerservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
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
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^\\+\\d+$", message = "Phone number must start with a plus sign and contain only digits.")
    private String phoneNumber;

    @NotEmpty(message = "Name is required")
    @Column // Nullable until the profile is completed
    private String name;

    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @Column
    private String profilePhoto; // URL or path to profile photo

    @Column
    private String homeAddress;

    @Column
    private String preferredPaymentMethod;

    @Column
    @Pattern(regexp = "^\\+\\d+$", message = "Phone number must start with a plus sign and contain only digits.")
    private String emergencyContact;

    @Column
    private String preferredLanguage;

    @NotNull
    @Column(columnDefinition = "BOOLEAN default false")
    private boolean isVerifiedPhoneNumber;

    @NotNull
    @Column(columnDefinition = "BOOLEAN default false")
    private boolean isProfileComplete = false; // Flag to check if the profile has name

    @Column
    private String verificationCode;

    @Column(nullable = false)
    private Integer balance = 0;

    public CustomerEntity() {
        // Default constructor
    }
}
