package com.allane.leasingapi.service;

import com.allane.leasingapi.model.ContractEntity;
import com.allane.leasingapi.model.CustomerEntity;
import com.allane.leasingapi.model.VehicleEntity;
import com.allane.leasingapi.repository.ContractRepository;
import com.allane.leasingapi.repository.CustomerRepository;
import com.allane.leasingapi.repository.VehicleRepository;

import java.util.Optional;
import java.util.Set;

public class Helper {

    private final VehicleRepository vehicleRepository;

    private final ContractRepository contractRepository;

    private final CustomerRepository customerRepository;

    public Helper(VehicleRepository vehicleRepository, ContractRepository contractRepository, CustomerRepository customerRepository) {

        this.vehicleRepository = vehicleRepository;
        this.contractRepository = contractRepository;
        this.customerRepository = customerRepository;
    }

    public Optional<CustomerEntity> findCustomerEntity(Long customerId) {

        return customerRepository.findById(customerId);
    }

    public Optional<VehicleEntity> findVehicleEntity(Long vehicleId) {

        return vehicleRepository.findById(vehicleId);
    }

    public Optional<ContractEntity> findContractEntity(Long contractNumber) {

        return contractRepository.findById(contractNumber);
    }

    public ContractEntity findContractEntityByVehicleId(Long vehicleId) {
        return contractRepository.findByVehicleId(vehicleId);
    }

    public Set<ContractEntity> findContractEntities(Long customerId) {

        return contractRepository.findByCustomerId(customerId);
    }
}
