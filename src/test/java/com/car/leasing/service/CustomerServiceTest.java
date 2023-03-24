package com.car.leasing.service;

import com.car.leasing.AbstractIT;
import com.car.leasing.dto.Customer;
import com.car.leasing.dto.Customers;
import com.car.leasing.dto.cruddto.CrudContractDto;
import com.car.leasing.dto.cruddto.CrudCustomerDto;
import com.car.leasing.exception.NotFoundException;
import com.car.leasing.model.ContractEntity;
import com.car.leasing.model.CustomerEntity;
import com.car.leasing.model.VehicleEntity;
import com.car.leasing.LeasingApiApplication;
import com.car.leasing.repository.ContractRepository;
import com.car.leasing.repository.CustomerRepository;
import com.car.leasing.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(classes = LeasingApiApplication.class)
@ActiveProfiles("test")
class CustomerServiceTest extends AbstractIT {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @SpyBean
    private CustomerService classToTest;

    @BeforeEach
    void cleanUp() {
        contractRepository.deleteAll();
        vehicleRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void create() {

        //Arrange
        CrudCustomerDto input = getCustomerDTO();

        //Act
        Customer customer = classToTest.create(input);

        //Assert;
        assertThat(customer.getFirstName()).isEqualTo("SOMETHING");
        assertThat(customer.getLastName()).isEqualTo("NOTHING");
        assertThat(customer.getBirthdate()).isEqualTo("2023-06-02");
        assertThat(customer.getContractList()).isNull();
    }

    @Test
    void find() {

        //Arrange
        CrudCustomerDto input = getCustomerDTO();

        //Act
        Customer customer = classToTest.create(input);
        Customer find = classToTest.find(customer.getCustomerId());

        //Assert
        assertThat(find.getFirstName()).isEqualTo("SOMETHING");
        assertThat(find.getLastName()).isEqualTo("NOTHING");
        assertThat(find.getBirthdate()).isEqualTo("2023-06-02");
        assertThat(find.getContractList()).isEmpty();
    }

    @Test
    void find_withContract() {

        //Arrange
        CrudCustomerDto input = getCustomerDTO();

        //Act
        Customer customer = classToTest.create(input);
        Customer find = classToTest.find(customer.getCustomerId());

        //Assert
        assertThat(find.getFirstName()).isEqualTo("SOMETHING");
        assertThat(find.getLastName()).isEqualTo("NOTHING");
        assertThat(find.getBirthdate()).isEqualTo("2023-06-02");
        assertThat(find.getContractList()).isEmpty();

        //Arrange
        CrudContractDto contractDTO = getContractDTO();
        contractDTO.setCustomerId(find.getCustomerId());

        VehicleEntity savedVehicle = vehicleRepository.save(getVehicleEntity());
        Optional<CustomerEntity> savedCustomer = customerRepository.findById(find.getCustomerId());

        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setMonthlyRate(246.75);
        contractEntity.setVehicleEntity(savedVehicle);
        contractEntity.setValidFrom(Date.valueOf("2023-05-01"));
        contractEntity.setValidUntil(Date.valueOf("2023-06-02"));
        contractEntity.setCustomerEntity(savedCustomer.get());

        ContractEntity contract = contractRepository.save(contractEntity);

        //Act
        Customer findAgain = classToTest.find(customer.getCustomerId());

        //Assert
        assertThat(findAgain.getFirstName()).isEqualTo("SOMETHING");
        assertThat(findAgain.getLastName()).isEqualTo("NOTHING");
        assertThat(findAgain.getBirthdate()).isEqualTo("2023-06-02");
        assertThat(findAgain.getContractList()).hasSize(1);

        List<Customer.Contract> list =  findAgain.getContractList().stream().toList();

        assertThat(list.get(0).getMonthlyRate()).isEqualTo(246.75);
        assertThat(list.get(0).getContractNumber()).isEqualTo(contract.getId());
    }

    @Test
    void findAll() {

        //Arrange
        CrudCustomerDto input = getCustomerDTO();

        //Act
        Customer customer = classToTest.create(input);
        Customer find = classToTest.find(customer.getCustomerId());

        CrudContractDto contractDTO = getContractDTO();
        contractDTO.setCustomerId(find.getCustomerId());

        VehicleEntity savedVehicle = vehicleRepository.save(getVehicleEntity());
        Optional<CustomerEntity> savedCustomer = customerRepository.findById(find.getCustomerId());

        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setMonthlyRate(246.75);
        contractEntity.setVehicleEntity(savedVehicle);
        contractEntity.setValidFrom(Date.valueOf("2023-05-01"));
        contractEntity.setValidUntil(Date.valueOf("2023-06-02"));
        contractEntity.setCustomerEntity(savedCustomer.get());

        ContractEntity contract = contractRepository.save(contractEntity);

        Customers findAll = classToTest.findAll();

        //Assert
        assertThat(findAll.getCustomerList().get(0).getFirstName()).isEqualTo("SOMETHING");
        assertThat(findAll.getCustomerList().get(0).getLastName()).isEqualTo("NOTHING");
        assertThat(findAll.getCustomerList().get(0).getBirthdate()).isEqualTo("2023-06-02");
        assertThat(findAll.getCustomerList().get(0).getContractList()).hasSize(1);

        List<Customer.Contract> list =  findAll.getCustomerList().get(0).getContractList().stream().toList();

        assertThat(list.get(0).getMonthlyRate()).isEqualTo(246.75);
        assertThat(list.get(0).getContractNumber()).isEqualTo(contract.getId());
    }

    @Test
    void findAll_EmptyList() {

        //Act
        Customers findAll = classToTest.findAll();

        //Assert
        assertThat(findAll.getCustomerList()).isEmpty();
    }

    @Test
    void find_NotFoundContract() {

        //Act & Assert
        assertThatThrownBy(() -> classToTest.find(3000000L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not Found with Customer ID: 3000000");
    }

    @Test
    void delete() {

        //Arrange
        CrudCustomerDto input = getCustomerDTO();

        //Act
        Customer customer = classToTest.create(input);
        Customer find = classToTest.find(customer.getCustomerId());

        Long customerId = find.getCustomerId();

        //Assert
        assertThat(find.getFirstName()).isEqualTo("SOMETHING");
        assertThat(find.getLastName()).isEqualTo("NOTHING");
        assertThat(find.getBirthdate()).isEqualTo("2023-06-02");
        assertThat(find.getContractList()).isEmpty();

        //Arrange
        CrudContractDto contractDTO = getContractDTO();
        contractDTO.setCustomerId(find.getCustomerId());

        VehicleEntity savedVehicle = vehicleRepository.save(getVehicleEntity());
        Optional<CustomerEntity> savedCustomer = customerRepository.findById(find.getCustomerId());

        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setMonthlyRate(246.75);
        contractEntity.setVehicleEntity(savedVehicle);
        contractEntity.setValidFrom(Date.valueOf("2023-05-01"));
        contractEntity.setValidUntil(Date.valueOf("2023-06-02"));
        contractEntity.setCustomerEntity(savedCustomer.get());

        ContractEntity contract = contractRepository.save(contractEntity);

        //Act
        classToTest.delete(savedCustomer.get().getId());

        Optional<ContractEntity> contractEntity1 = contractRepository.findById(contract.getId());

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(savedVehicle.getId());

        //Act & Assert

        assertThat(contractEntity1.get().getCustomerEntity()).isNull();
        assertThat(contractEntity1.get().getMonthlyRate()).isEqualTo(246.75);

        assertThat(vehicleEntity.get().getId()).isEqualTo(savedVehicle.getId());
        assertThat(vehicleEntity.get().getBrand()).isEqualTo("ANYTHING");
        assertThat(vehicleEntity.get().getModel()).isEqualTo("SOMETHING");
        assertThat(vehicleEntity.get().getModelYear()).isEqualTo(2006);
        assertThat(vehicleEntity.get().getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
        assertThat(vehicleEntity.get().getPrice()).isEqualTo(0.0);
        assertThat(vehicleEntity.get().getContractEntity().getId()).isEqualTo(contractEntity1.get().getId());

        assertThatThrownBy(() -> classToTest.find(customerId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not Found with Customer ID: " + customerId);
    }

    @Test
    void Update() {

        //Arrange
        CustomerEntity savedCustomer = customerRepository.save(getCustomerEntity());

        assertThat(savedCustomer.getFirstName()).isEqualTo("SOMETHING");
        assertThat(savedCustomer.getLastName()).isEqualTo("NOTHING");
        assertThat(savedCustomer.getBirthdate()).isEqualTo("2023-06-02");

        CrudCustomerDto input = getCustomerDTO();
        input.setFirstName("ANYTHING");
        input.setLastName("EVERYTHING");
        input.setBirthdate(Date.valueOf("1923-06-02"));

        //Act
        Customer customer = classToTest.update(savedCustomer.getId(), input);

        //Assert
        assertThat(customer.getCustomerId()).isEqualTo(savedCustomer.getId());
        assertThat(customer.getFirstName()).isEqualTo("ANYTHING");
        assertThat(customer.getLastName()).isEqualTo("EVERYTHING");
        assertThat(customer.getBirthdate()).isEqualTo("1923-06-02");
    }

    @Test
    void update_NotFound() {

        //Arrange
        CrudCustomerDto crudCustomerDto = getCustomerDTO();

        //Act & Assert
        assertThatThrownBy(() -> classToTest.update(3000000000L, crudCustomerDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not Found with Customer ID: 3000000000");
    }


    private CrudCustomerDto getCustomerDTO() {

        CrudCustomerDto crudCustomerDto = new CrudCustomerDto();

        crudCustomerDto.setFirstName("SOMETHING");
        crudCustomerDto.setLastName("NOTHING");
        crudCustomerDto.setBirthdate(Date.valueOf("2023-06-02"));

        return crudCustomerDto;
    }

    private CustomerEntity getCustomerEntity() {

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1001L);
        customerEntity.setFirstName("SOMETHING");
        customerEntity.setLastName("NOTHING");
        customerEntity.setBirthdate(Date.valueOf("2023-06-02"));

        return customerEntity;
    }

    private VehicleEntity getVehicleEntity() {

        VehicleEntity vehicleEntity = new VehicleEntity();

        vehicleEntity.setId(20007L);
        vehicleEntity.setBrand("ANYTHING");
        vehicleEntity.setModel("SOMETHING");
        vehicleEntity.setPrice(0.0);
        vehicleEntity.setVehicleIdentificationNumber("EVERYTHING");
        vehicleEntity.setModelYear(2006);

        return vehicleEntity;
    }

    private CrudContractDto getContractDTO() {
        CrudContractDto crudContractDto = new CrudContractDto();
        crudContractDto.setMonthlyRate(246.75);
        crudContractDto.setValidFrom(Date.valueOf("2023-05-01"));
        crudContractDto.setValidUntil(Date.valueOf("2023-06-02"));
        crudContractDto.setCustomerId(1001L);
        crudContractDto.setVehicleId(20007L);

        return crudContractDto;
    }
}
