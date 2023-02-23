package com.allane.leasingapi.controller.converter;

import com.allane.leasingapi.dto.Contract;
import com.allane.leasingapi.model.ContractEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class ContractConverter implements Converter<ContractEntity, Contract> {

    @Override
    public Contract convert(ContractEntity contractEntity) {

        return Contract.builder()
                .contractNumber(contractEntity.getId())
                .monthlyRate(contractEntity.getMonthlyRate())
                .validFrom(new SimpleDateFormat("yyyy-MM-dd").format(contractEntity.getValidFrom()))
                .validUntil(new SimpleDateFormat("yyyy-MM-dd").format(contractEntity.getValidUntil()))
                .vehicle(contractEntity.getVehicleEntity() != null ? Contract.Vehicle.builder()
                        .vehicleId(contractEntity.getVehicleEntity().getId())
                        .brand(contractEntity.getVehicleEntity().getBrand())
                        .model(contractEntity.getVehicleEntity().getModel())
                        .modelYear(contractEntity.getVehicleEntity().getModelYear())
                        .vehicleIdentificationNumber(contractEntity.getVehicleEntity().getVehicleIdentificationNumber())
                        .build() : null)
                .customer(contractEntity.getCustomerEntity() != null ? Contract.Customer.builder()
                        .customerId(contractEntity.getCustomerEntity().getId())
                        .firstName(contractEntity.getCustomerEntity().getFirstName())
                        .lastName(contractEntity.getCustomerEntity().getLastName())
                        .build() : null)
                .build();
    }
}
