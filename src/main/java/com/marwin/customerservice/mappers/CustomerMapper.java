package com.marwin.customerservice.mappers;

import com.marwin.customerservice.entity.CustomerEntity;
import com.marwin.customerservice.models.CreateCustomerDTO;
import com.marwin.customerservice.models.CustomerDTO;
import org.mapstruct.Mapper;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerEntity createCustomerDTOToCustomerEntity(CreateCustomerDTO customerDTO);

    CustomerDTO customerEntityToCustomerDTO(CustomerEntity customerEntity);

    List<CustomerDTO> customerEntitiesToCustomerDTO(List<CustomerEntity> customerEntities);
}
