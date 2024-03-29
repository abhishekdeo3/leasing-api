package com.car.leasing.service;

import com.car.leasing.exception.BadRequestException;
import com.car.leasing.exception.NotFoundException;
import com.car.leasing.model.ContractEntity;
import com.car.leasing.controller.converter.Converter;
import com.car.leasing.dto.Contract;
import com.car.leasing.dto.Contracts;
import com.car.leasing.dto.cruddto.CrudContractDto;
import com.car.leasing.repository.ContractRepository;
import com.car.leasing.repository.CustomerRepository;
import com.car.leasing.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService extends Helper implements CRUDOperation<Contract, CrudContractDto, Long, Contracts> {

    private static final String CONTRACT = "Contract";

    private final ContractRepository contractRepository;

    private final Converter<ContractEntity, Contract> contractConverter;

    public ContractService(ContractRepository contractRepository, CustomerRepository customerRepository,
                           VehicleRepository vehicleRepository, Converter<ContractEntity, Contract> contractConverter) {
        super(vehicleRepository, contractRepository, customerRepository);
        this.contractRepository = contractRepository;
        this.contractConverter = contractConverter;
    }


    @Override
    @Transactional
    public Contract create(CrudContractDto crudContractDto) {

        ContractEntity contractEntity = new ContractEntity();

        contractEntity.setMonthlyRate(crudContractDto.getMonthlyRate());

        if(crudContractDto.getValidFrom().after(crudContractDto.getValidUntil())) {
            throw new BadRequestException("Valid Until Date Should be Greater than Valid From Date");
        }

        contractEntity.setCustomerEntity(crudContractDto.getCustomerId() != null ?
                findCustomerEntity(crudContractDto.getCustomerId())
                        .orElseThrow(() -> new NotFoundException("Customer", crudContractDto.getCustomerId())) : null);

        contractEntity.setVehicleEntity(crudContractDto.getVehicleId() != null ?
                findVehicleEntity(crudContractDto.getVehicleId())
                        .orElseThrow(() -> new NotFoundException("Vehicle", crudContractDto.getVehicleId())) : null);

        contractEntity.setValidFrom(crudContractDto.getValidFrom());
        contractEntity.setValidUntil(crudContractDto.getValidUntil());

        ContractEntity persistedContractEntity = contractRepository.save(contractEntity);

        return contractConverter.convert(persistedContractEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Contract find(Long contractNumber) {

        Optional<ContractEntity> contractEntity = findContractEntity(contractNumber);

        return contractEntity.map(contractConverter::convert)
                .orElseThrow(() -> new NotFoundException(CONTRACT, contractNumber));
    }

    @Override
    @Transactional
    public Contract update(Long contractNumber, CrudContractDto crudContractDto) {

        ContractEntity contractEntity = findContractEntity(contractNumber)
                .orElseThrow(() -> new NotFoundException(CONTRACT, contractNumber));

        if(crudContractDto.getValidFrom().after(crudContractDto.getValidUntil())) {
            throw new BadRequestException("Valid Until Date Should be Greater than Valid From Date");
        }

        contractEntity.setMonthlyRate(crudContractDto.getMonthlyRate());
        contractEntity.setValidFrom(crudContractDto.getValidFrom());
        contractEntity.setValidUntil(crudContractDto.getValidUntil());

        contractEntity.setCustomerEntity(crudContractDto.getCustomerId() != null ?
                findCustomerEntity(crudContractDto.getCustomerId())
                        .orElseThrow(() -> new NotFoundException("Customer", crudContractDto.getCustomerId())) : null);

        contractEntity.setVehicleEntity(crudContractDto.getVehicleId() != null ?
                findVehicleEntity(crudContractDto.getVehicleId())
                        .orElseThrow(() -> new NotFoundException("Vehicle", crudContractDto.getVehicleId())) : null);

        ContractEntity saved = contractRepository.save(contractEntity);

        return contractConverter.convert(saved);
    }

    @Override
    @Transactional
    public void delete(Long contractNumber) {

        ContractEntity contractEntity = findContractEntity(contractNumber)
                .orElseThrow(() -> new NotFoundException(CONTRACT, contractNumber));

        contractEntity.setVehicleEntity(null);
        contractEntity.setCustomerEntity(null);

        contractRepository.deleteById(contractEntity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Contracts findAll() {

        List<ContractEntity> contractEntities = contractRepository.findAll();
        List<Contract> contractList = contractEntities.stream().map(contractConverter::convert).toList();
        Contracts contracts = new Contracts();
        contracts.setContractList(contractList);

        return contracts;
    }
}
