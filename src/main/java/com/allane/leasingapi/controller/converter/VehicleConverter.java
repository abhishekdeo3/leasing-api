package com.allane.leasingapi.controller.converter;

import com.allane.leasingapi.dto.Vehicle;
import com.allane.leasingapi.model.ContractEntity;
import com.allane.leasingapi.model.VehicleEntity;
import org.springframework.stereotype.Component;

@Component
public class VehicleConverter implements Converter<VehicleEntity, Vehicle> {

    @Override
    public Vehicle convert(VehicleEntity vehicleEntity) {

        return Vehicle.builder()
                .vehicleId(vehicleEntity.getId())
                .brand(vehicleEntity.getBrand())
                .model(vehicleEntity.getModel())
                .price(vehicleEntity.getPrice())
                .year(vehicleEntity.getModelYear())
                .vehicleIdentificationNumber(vehicleEntity.getVehicleIdentificationNumber())
                .contract(vehicleEntity.getContractEntity() != null ? convert(vehicleEntity.getContractEntity()) : null)
                .build();
    }

    private Vehicle.Contract convert(ContractEntity contractEntity) {

        return Vehicle.Contract.builder()
                .contractNumber(contractEntity.getId())
                .monthlyRate(contractEntity.getMonthlyRate())
                .build();
    }
}