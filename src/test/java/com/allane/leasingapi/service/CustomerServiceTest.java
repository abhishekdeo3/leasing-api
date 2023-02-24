package com.allane.leasingapi.service;

import com.allane.leasingapi.AbstractIT;
import com.allane.leasingapi.LeasingApiApplication;
import com.allane.leasingapi.dto.Customer;
import com.allane.leasingapi.dto.Customers;
import com.allane.leasingapi.dto.cruddto.CrudContractDto;
import com.allane.leasingapi.dto.cruddto.CrudCustomerDto;
import com.allane.leasingapi.exception.NotFoundException;
import com.allane.leasingapi.model.ContractEntity;
import com.allane.leasingapi.model.CustomerEntity;
import com.allane.leasingapi.model.VehicleEntity;
import com.allane.leasingapi.repository.ContractRepository;
import com.allane.leasingapi.repository.CustomerRepository;
import com.allane.leasingapi.repository.VehicleRepository;
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

//    @Test
//    void Update() {
//
//        //Arrange
//        CrudContractDto input = getContractDTO();
//
//        VehicleEntity savedVehicle = vehicleRepository.save(getVehicleEntity());
//        CustomerEntity savedCustomer = customerRepository.save(getCustomerEntity());
//
//        when(spy(classToTest).findCustomerEntity(input.getCustomerId())).thenReturn(Optional.of(savedCustomer));
//        when(spy(classToTest).findVehicleEntity(input.getVehicleId())).thenReturn(Optional.of(savedVehicle));
//
//        //Act
//        Contract contract = classToTest.create(input);
//
//        //Assert
//        assertThat(contract.getMonthlyRate()).isEqualTo(246.75);
//        assertThat(contract.getValidFrom()).isEqualTo("2023-05-01");
//        assertThat(contract.getValidUntil()).isEqualTo("2023-06-02");
//
//        assertThat(contract.getCustomer().getCustomerId()).isEqualTo(savedCustomer.getId());
//        assertThat(contract.getCustomer().getFirstName()).isEqualTo("SOMETHING");
//        assertThat(contract.getCustomer().getLastName()).isEqualTo("NOTHING");
//
//        assertThat(contract.getVehicle().getVehicleId()).isEqualTo(savedVehicle.getId());
//        assertThat(contract.getVehicle().getBrand()).isEqualTo("ANYTHING");
//        assertThat(contract.getVehicle().getModel()).isEqualTo("SOMETHING");
//        assertThat(contract.getVehicle().getModelYear()).isEqualTo(2006);
//        assertThat(contract.getVehicle().getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
//
//        //Act
//        CrudContractDto crudContractDto = getCrudContractDto();
//
//        VehicleEntity vehicleEntityToUpdate = getVehicleEntity();
//        vehicleEntityToUpdate.setId(null);
//        vehicleEntityToUpdate.setVehicleIdentificationNumber("NOTHING");
//
//        CustomerEntity customerEntityToUpdate = getCustomerEntity();
//        customerEntityToUpdate.setId(null);
//
//        VehicleEntity updatedVehicle = vehicleRepository.save(vehicleEntityToUpdate);
//        CustomerEntity updatedCustomer = customerRepository.save(customerEntityToUpdate);
//
//        when(spy(classToTest).findCustomerEntity(customerEntityToUpdate.getId())).thenReturn(Optional.of(updatedCustomer));
//        when(spy(classToTest).findVehicleEntity(updatedVehicle.getId())).thenReturn(Optional.of(updatedVehicle));
//
//        crudContractDto.setCustomerId(updatedCustomer.getId());
//        crudContractDto.setVehicleId(updatedVehicle.getId());
//
//        Contract update = classToTest.update(contract.getContractNumber(), crudContractDto);
//
//        assertThat(update.getMonthlyRate()).isEqualTo(500.89);
//        assertThat(update.getValidFrom()).isEqualTo("2022-12-31");
//        assertThat(update.getValidUntil()).isEqualTo("2026-12-30");
//        assertThat(update.getVehicle().getVehicleId()).isEqualTo(updatedVehicle.getId());
//        assertThat(update.getCustomer().getCustomerId()).isEqualTo(updatedCustomer.getId());
//    }
//
//    @Test
//    void Update_withNull() {
//
//        //Arrange
//        CrudContractDto input = getContractDTO();
//
//        VehicleEntity savedVehicle = vehicleRepository.save(getVehicleEntity());
//        CustomerEntity savedCustomer = customerRepository.save(getCustomerEntity());
//
//        when(spy(classToTest).findCustomerEntity(input.getCustomerId())).thenReturn(Optional.of(savedCustomer));
//        when(spy(classToTest).findVehicleEntity(input.getVehicleId())).thenReturn(Optional.of(savedVehicle));
//
//        //Act
//        Contract contract = classToTest.create(input);
//
//        //Assert
//        assertThat(contract.getMonthlyRate()).isEqualTo(246.75);
//        assertThat(contract.getValidFrom()).isEqualTo("2023-05-01");
//        assertThat(contract.getValidUntil()).isEqualTo("2023-06-02");
//
//        assertThat(contract.getCustomer().getCustomerId()).isEqualTo(savedCustomer.getId());
//        assertThat(contract.getCustomer().getFirstName()).isEqualTo("SOMETHING");
//        assertThat(contract.getCustomer().getLastName()).isEqualTo("NOTHING");
//
//        assertThat(contract.getVehicle().getVehicleId()).isEqualTo(savedVehicle.getId());
//        assertThat(contract.getVehicle().getBrand()).isEqualTo("ANYTHING");
//        assertThat(contract.getVehicle().getModel()).isEqualTo("SOMETHING");
//        assertThat(contract.getVehicle().getModelYear()).isEqualTo(2006);
//        assertThat(contract.getVehicle().getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
//
//        //Act
//        CrudContractDto crudContractDto = getCrudContractDto();
//
//        crudContractDto.setCustomerId(null);
//        crudContractDto.setVehicleId(null);
//
//        Contract update = classToTest.update(contract.getContractNumber(), crudContractDto);
//
//        assertThat(update.getMonthlyRate()).isEqualTo(500.89);
//        assertThat(update.getValidFrom()).isEqualTo("2022-12-31");
//        assertThat(update.getValidUntil()).isEqualTo("2026-12-30");
//        assertThat(update.getVehicle()).isNull();
//        assertThat(update.getCustomer()).isNull();
//    }

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

    private CrudContractDto getCrudContractDto() {

        CrudContractDto crudContractDto = new CrudContractDto();
        crudContractDto.setMonthlyRate(500.89);
        crudContractDto.setValidFrom(Date.valueOf("2022-12-31"));
        crudContractDto.setValidUntil(Date.valueOf("2026-12-30"));
        crudContractDto.setCustomerId(1002L);
        crudContractDto.setVehicleId(20008L);

        return crudContractDto;
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
