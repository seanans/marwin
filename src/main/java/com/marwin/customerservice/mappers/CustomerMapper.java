package com.marwin.customerservice.mappers;

import com.marwin.customerservice.entity.CustomerEntity;
import com.marwin.customerservice.models.CreateCustomerDTO;
import org.mapstruct.Mapper;

import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerEntity createCustomerDTOToCustomerEntity(CreateCustomerDTO customerDTO);
}
