package com.marwin.customerservice.repository;

import com.marwin.customerservice.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID>, JpaSpecificationExecutor<CustomerEntity> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    CustomerEntity getCustomerEntityByPhoneNumber(String phoneNumber);
}

