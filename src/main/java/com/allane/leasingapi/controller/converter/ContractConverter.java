package com.allane.leasingapi.controller.converter;

import com.allane.leasingapi.dto.Contract;
import com.allane.leasingapi.model.ContractEntity;
import org.springframework.stereotype.Component;

@Component
public class ContractConverter implements Converter<ContractEntity, Contract> {

    @Override
    public Contract convert(ContractEntity contractEntity) {

        return Contract.builder()
                .contractNumber(contractEntity.getId())
                .monthlyRate(contractEntity.getMonthlyRate())
                .vehicle(Contract.Vehicle.builder()
                        .vehicleId(contractEntity.getVehicleEntity().getId())
                        .brand(contractEntity.getVehicleEntity().getBrand())
                        .model(contractEntity.getVehicleEntity().getModel())
                        .modelYear(contractEntity.getVehicleEntity().getModelYear())
                        .vehicleIdentificationNumber(contractEntity.getVehicleEntity().getVehicleIdentificationNumber())
                        .build())
                .customer(Contract.Customer.builder()
                        .customerId(contractEntity.getCustomerEntity().getId())
                        .firstName(contractEntity.getCustomerEntity().getFirstName())
                        .lastName(contractEntity.getCustomerEntity().getLastName())
                        .build())
                .build();
    }
}
