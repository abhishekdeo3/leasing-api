package com.allane.leasingapi.service;

import com.allane.leasingapi.controller.converter.Converter;
import com.allane.leasingapi.dto.Contract;
import com.allane.leasingapi.dto.Contracts;
import com.allane.leasingapi.dto.cruddto.CrudContractDto;
import com.allane.leasingapi.exception.NotFoundException;
import com.allane.leasingapi.model.ContractEntity;
import com.allane.leasingapi.repository.ContractRepository;
import com.allane.leasingapi.repository.CustomerRepository;
import com.allane.leasingapi.repository.VehicleRepository;
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
        contractEntity.setCustomerEntity(findCustomerEntity(crudContractDto.getCustomerId()).orElse(null));
        contractEntity.setVehicleEntity(findVehicleEntity(crudContractDto.getVehicleId()).orElse(null));
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

        contractEntity.setMonthlyRate(crudContractDto.getMonthlyRate());
        contractEntity.setValidFrom(crudContractDto.getValidFrom());
        contractEntity.setValidUntil(crudContractDto.getValidUntil());
        contractEntity.setCustomerEntity(crudContractDto.getCustomerId() != null ?
                findCustomerEntity(crudContractDto.getCustomerId()).orElse(null) : null);
        contractEntity.setVehicleEntity(crudContractDto.getVehicleId() != null ?
                findVehicleEntity(crudContractDto.getVehicleId()).orElse(null) : null);

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
