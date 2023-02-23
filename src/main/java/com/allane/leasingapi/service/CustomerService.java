package com.allane.leasingapi.service;

import com.allane.leasingapi.controller.converter.Converter;
import com.allane.leasingapi.dto.Customer;
import com.allane.leasingapi.dto.Customers;
import com.allane.leasingapi.dto.cruddto.CrudCustomerDto;
import com.allane.leasingapi.exception.NotFoundException;
import com.allane.leasingapi.model.ContractEntity;
import com.allane.leasingapi.model.CustomerEntity;
import com.allane.leasingapi.repository.ContractRepository;
import com.allane.leasingapi.repository.CustomerRepository;
import com.allane.leasingapi.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomerService extends Helper implements CRUDOperation<Customer, CrudCustomerDto, Long, Customers> {

    private static final String CUSTOMER = "Customer";
    private final CustomerRepository customerRepository;

    private final ContractRepository contractRepository;

    private final Converter<CustomerEntity, Customer> customerConverter;

    public CustomerService(VehicleRepository vehicleRepository, CustomerRepository customerRepository,
                           ContractRepository contractRepository, Converter<CustomerEntity, Customer> customerConverter) {
        super(vehicleRepository, contractRepository, customerRepository);
        this.customerRepository = customerRepository;
        this.contractRepository = contractRepository;
        this.customerConverter = customerConverter;
    }


    @Override
    @Transactional(readOnly = true)
    public Customer find(Long customerId) {

        Optional<CustomerEntity> customerEntity = findCustomerEntity(customerId);

        return customerEntity.map(customerConverter::convert)
                .orElseThrow(() -> new NotFoundException(CUSTOMER, customerId));
    }

    @Override
    @Transactional
    public Customer create(CrudCustomerDto crudCustomerDto) {

        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setFirstName(crudCustomerDto.getFirstName());
        customerEntity.setLastName(crudCustomerDto.getLastName());
        customerEntity.setBirthdate(crudCustomerDto.getBirthdate());

        CustomerEntity persistedContractEntity = customerRepository.save(customerEntity);

        return customerConverter.convert(persistedContractEntity);
    }

    @Override
    @Transactional
    public void delete(Long customerId) {

        CustomerEntity customerEntity = findCustomerEntity(customerId)
                .orElseThrow(() -> new NotFoundException(CUSTOMER, customerId));

        Set<ContractEntity> contractEntities = findContractEntities(customerId);

        if (!contractEntities.isEmpty()) {

            contractEntities.forEach(contractEntity -> {
                contractEntity.setCustomerEntity(null);
                contractRepository.save(contractEntity);
            });
        }

        customerRepository.deleteById(customerEntity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Customers findAll() {

        List<CustomerEntity> customerEntities = customerRepository.findAll();
        List<Customer> customerList = customerEntities.stream().map(customerConverter::convert).toList();
        Customers customers = new Customers();
        customers.setCustomerList(customerList);

        return customers;
    }

    @Override
    @Transactional
    public Customer update(Long customerId, CrudCustomerDto crudCustomerDto) {

        CustomerEntity customerEntity = findCustomerEntity(customerId)
                .orElseThrow(() -> new NotFoundException(CUSTOMER, customerId));

        customerEntity.setFirstName(crudCustomerDto.getFirstName());
        customerEntity.setLastName(crudCustomerDto.getLastName());
        customerEntity.setBirthdate(crudCustomerDto.getBirthdate());
        customerEntity.setLeasingContractEntities(findContractEntities(customerId));

        CustomerEntity saved = customerRepository.save(customerEntity);

        return customerConverter.convert(saved);
    }
}
