package com.allane.leasingapi.controller.converter;

import com.allane.leasingapi.dto.Contract;
import com.allane.leasingapi.model.ContractEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class ContractConverter implements Converter<ContractEntity, Contract> {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public Contract convert(ContractEntity contractEntity) {

        return Contract.builder()
                .contractNumber(contractEntity.getId())
                .monthlyRate(contractEntity.getMonthlyRate())
                .validFrom(new SimpleDateFormat(DATE_PATTERN).format(contractEntity.getValidFrom()))
                .validUntil(new SimpleDateFormat(DATE_PATTERN).format(contractEntity.getValidUntil()))
                .vehicle(contractEntity.getVehicleEntity() != null ? Contract.Vehicle.builder()
                        .vehicleId(contractEntity.getVehicleEntity().getId())
                        .brand(contractEntity.getVehicleEntity().getBrand())
                        .model(contractEntity.getVehicleEntity().getModel())
                        .modelYear(contractEntity.getVehicleEntity().getModelYear())
                        .vehicleIdentificationNumber(contractEntity.getVehicleEntity().getVehicleIdentificationNumber())
                        .price(contractEntity.getVehicleEntity().getPrice())
                        .build() : null)
                .customer(contractEntity.getCustomerEntity() != null ? Contract.Customer.builder()
                        .customerId(contractEntity.getCustomerEntity().getId())
                        .firstName(contractEntity.getCustomerEntity().getFirstName())
                        .lastName(contractEntity.getCustomerEntity().getLastName())
                        .birthDate(new SimpleDateFormat(DATE_PATTERN).format(contractEntity.getCustomerEntity().getBirthdate()))
                        .build() : null)
                .build();
    }
}
