package com.allane.leasingapi.service;

import com.allane.leasingapi.controller.converter.Converter;
import com.allane.leasingapi.dto.Vehicle;
import com.allane.leasingapi.dto.Vehicles;
import com.allane.leasingapi.dto.cruddto.CrudVehicleDto;
import com.allane.leasingapi.exception.BadRequestException;
import com.allane.leasingapi.model.ContractEntity;
import com.allane.leasingapi.model.VehicleEntity;
import com.allane.leasingapi.repository.ContractRepository;
import com.allane.leasingapi.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehicleService implements CRUDOperation<Vehicle, CrudVehicleDto, Long, Vehicles> {

    private final VehicleRepository vehicleRepository;

    private final ContractRepository contractRepository;

    private final Converter<VehicleEntity, Vehicle> vehicleConverter;

    public VehicleService(VehicleRepository vehicleRepository, ContractRepository contractRepository,
                          Converter<VehicleEntity, Vehicle> vehicleConverter) {
        this.vehicleRepository = vehicleRepository;
        this.contractRepository = contractRepository;
        this.vehicleConverter = vehicleConverter;
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicle find(Long vehicleId) {

        return vehicleConverter.convert(findVehicleEntity(vehicleId));
    }

    @Override
    @Transactional
    public Vehicle create(CrudVehicleDto crudVehicleDto) {

        VehicleEntity vehicleEntity = new VehicleEntity();

        vehicleEntity.setBrand(crudVehicleDto.getBrand());
        vehicleEntity.setModel(crudVehicleDto.getModel());
        vehicleEntity.setModelYear(crudVehicleDto.getModelYear());
        vehicleEntity.setPrice(crudVehicleDto.getPrice());
        vehicleEntity.setVehicleIdentificationNumber(crudVehicleDto.getVehicleIdentificationNumber());

        VehicleEntity persistedContractEntity = vehicleRepository.save(vehicleEntity);

        return vehicleConverter.convert(persistedContractEntity);
    }

    @Override
    @Transactional
    public void delete(Long vehicleId) {

        VehicleEntity vehicleEntity = findVehicleEntity(vehicleId);
        vehicleRepository.deleteById(vehicleEntity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicles findAll() {

        List<VehicleEntity> vehicleEntities = vehicleRepository.findAll();
        List<Vehicle> vehicleList = vehicleEntities.stream().map(vehicleConverter::convert).toList();
        Vehicles vehicles = new Vehicles();
        vehicles.setVehicleList(vehicleList);

        return vehicles;
    }

    @Override
    @Transactional
    public Vehicle update(Long vehicleId, CrudVehicleDto crudVehicleDto) {

        VehicleEntity vehicleEntity = findVehicleEntity(vehicleId);

        vehicleEntity.setBrand(crudVehicleDto.getBrand());
        vehicleEntity.setModel(crudVehicleDto.getModel());
        vehicleEntity.setModelYear(crudVehicleDto.getModelYear());
        vehicleEntity.setPrice(crudVehicleDto.getPrice());
        vehicleEntity.setVehicleIdentificationNumber(crudVehicleDto.getVehicleIdentificationNumber());
        vehicleEntity.setContractEntity(findContractEntity(vehicleId));

        VehicleEntity saved = vehicleRepository.save(vehicleEntity);

        return vehicleConverter.convert(saved);
    }

    private VehicleEntity findVehicleEntity(Long vehicleId) {

        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new BadRequestException("Vehicle Not Found for ID: " + vehicleId));
    }

    private ContractEntity findContractEntity(Long vehicleId) {
        return contractRepository.findByVehicleId(vehicleId);
    }
}
