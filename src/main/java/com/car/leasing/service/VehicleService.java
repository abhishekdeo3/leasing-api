package com.car.leasing.service;

import com.car.leasing.exception.NotFoundException;
import com.car.leasing.model.ContractEntity;
import com.car.leasing.controller.converter.Converter;
import com.car.leasing.dto.Vehicle;
import com.car.leasing.dto.Vehicles;
import com.car.leasing.dto.cruddto.CrudVehicleDto;
import com.car.leasing.model.VehicleEntity;
import com.car.leasing.repository.ContractRepository;
import com.car.leasing.repository.CustomerRepository;
import com.car.leasing.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService extends Helper implements CRUDOperation<Vehicle, CrudVehicleDto, Long, Vehicles> {

    private static final String VEHICLE = "Vehicle";

    private final VehicleRepository vehicleRepository;

    private final ContractRepository contractRepository;

    private final Converter<VehicleEntity, Vehicle> vehicleConverter;

    public VehicleService(VehicleRepository vehicleRepository, ContractRepository contractRepository,
                          Converter<VehicleEntity, Vehicle> vehicleConverter, CustomerRepository customerRepository) {
        super(vehicleRepository, contractRepository, customerRepository);
        this.vehicleRepository = vehicleRepository;
        this.contractRepository = contractRepository;
        this.vehicleConverter = vehicleConverter;
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicle find(Long vehicleId) {

        Optional<VehicleEntity> vehicleEntity = findVehicleEntity(vehicleId);

        return vehicleEntity.map(vehicleConverter::convert)
                .orElseThrow(() -> new NotFoundException(VEHICLE, vehicleId));
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

        VehicleEntity vehicleEntity = findVehicleEntity(vehicleId)
                .orElseThrow(() -> new NotFoundException(VEHICLE, vehicleId));

        ContractEntity contractEntity = findContractEntityByVehicleId(vehicleId);

        if (contractEntity != null) {

            contractEntity.setVehicleEntity(null);
            contractRepository.save(contractEntity);
        }

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

        VehicleEntity vehicleEntity = findVehicleEntity(vehicleId)
                .orElseThrow(() -> new NotFoundException(VEHICLE, vehicleId));

        vehicleEntity.setBrand(crudVehicleDto.getBrand());
        vehicleEntity.setModel(crudVehicleDto.getModel());
        vehicleEntity.setModelYear(crudVehicleDto.getModelYear());
        vehicleEntity.setPrice(crudVehicleDto.getPrice());
        vehicleEntity.setVehicleIdentificationNumber(crudVehicleDto.getVehicleIdentificationNumber());

        vehicleEntity.setContractEntity(findContractEntityByVehicleId(vehicleId) != null ?
                findContractEntityByVehicleId(vehicleId) : null);

        VehicleEntity saved = vehicleRepository.save(vehicleEntity);

        return vehicleConverter.convert(saved);
    }
}
