package com.allane.leasingapi.service;

import com.allane.leasingapi.controller.converter.Converter;
import com.allane.leasingapi.dto.Contract;
import com.allane.leasingapi.dto.Contracts;
import com.allane.leasingapi.dto.cruddto.CrudContractDto;
import com.allane.leasingapi.exception.BadRequestException;
import com.allane.leasingapi.exception.NotFoundException;
import com.allane.leasingapi.model.ContractEntity;
import com.allane.leasingapi.model.CustomerEntity;
import com.allane.leasingapi.model.VehicleEntity;
import com.allane.leasingapi.repository.ContractRepository;
import com.allane.leasingapi.repository.CustomerRepository;
import com.allane.leasingapi.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContractService implements CRUDOperation<Contract, CrudContractDto, Long, Contracts> {

    private final ContractRepository contractRepository;

    private final CustomerRepository customerRepository;

    private final VehicleRepository vehicleRepository;

    private final Converter<ContractEntity, Contract> contractConverter;

    public ContractService(ContractRepository contractRepository, CustomerRepository customerRepository,
                           VehicleRepository vehicleRepository, Converter<ContractEntity, Contract> contractConverter) {
        this.contractRepository = contractRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
        this.contractConverter = contractConverter;
    }

    @Override
    @Transactional
    public Contract create(CrudContractDto crudContractDto) {

        ContractEntity contractEntity = new ContractEntity();

        contractEntity.setMonthlyRate(crudContractDto.getMonthlyRate());
        contractEntity.setCustomerEntity(findCustomerEntity(crudContractDto.getCustomerId()));
        contractEntity.setVehicleEntity(findVehicleEntity(crudContractDto.getVehicleId()));
        contractEntity.setValidFrom(crudContractDto.getValidFrom());
        contractEntity.setValidUntil(crudContractDto.getValidUntil());

        ContractEntity persistedContractEntity = contractRepository.save(contractEntity);

        return contractConverter.convert(persistedContractEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Contract find(Long contractNumber) {

        return contractConverter.convert(findContractEntity(contractNumber));
    }

    @Override
    @Transactional
    public Contract update(Long contractNumber, CrudContractDto crudContractDto) {

        ContractEntity contractEntity = findContractEntity(contractNumber);

        contractEntity.setMonthlyRate(crudContractDto.getMonthlyRate());
        contractEntity.setValidFrom(crudContractDto.getValidFrom());
        contractEntity.setValidUntil(crudContractDto.getValidUntil());
        contractEntity.setCustomerEntity(findCustomerEntity(crudContractDto.getCustomerId()));
        contractEntity.setVehicleEntity(findVehicleEntity(crudContractDto.getVehicleId()));

        ContractEntity saved = contractRepository.save(contractEntity);

        return contractConverter.convert(saved);

    }

    @Override
    @Transactional
    public void delete(Long contractNumber) {

        ContractEntity contractEntity = findContractEntity(contractNumber);
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

    private CustomerEntity findCustomerEntity(Long customerId) {

        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BadRequestException("Customer Not Found for ID: " + customerId));
    }

    private VehicleEntity findVehicleEntity(Long vehicleId) {

        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new BadRequestException("Vehicle Not Found for ID: " + vehicleId));
    }

    private ContractEntity findContractEntity(Long contractNumber) {

        return contractRepository.findById(contractNumber)
                .orElseThrow(() -> new NotFoundException("Not found with Contract Number =" + contractNumber));
    }
}
