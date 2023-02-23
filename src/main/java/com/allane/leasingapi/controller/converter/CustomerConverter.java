package com.allane.leasingapi.controller.converter;

import com.allane.leasingapi.dto.Customer;
import com.allane.leasingapi.model.ContractEntity;
import com.allane.leasingapi.model.CustomerEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

@Component
public class CustomerConverter implements Converter<CustomerEntity, Customer> {

    @Override
    public Customer convert(CustomerEntity customerEntity) {

        return Customer.builder()
                .customerId(customerEntity.getId())
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .birthdate(new SimpleDateFormat("yyyy-MM-dd").format(customerEntity.getBirthdate()))
                .contractList(customerEntity.getLeasingContractEntities() != null ?
                        customerEntity.getLeasingContractEntities().stream().map(this::convert)
                                .collect(Collectors.toSet()) : null)
                .build();
    }

    private Customer.Contract convert(ContractEntity contractEntity) {

        return Customer.Contract.builder()
                .contractNumber(contractEntity.getId())
                .monthlyRate(contractEntity.getMonthlyRate())
                .build();
    }
}
