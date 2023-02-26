package com.allane.leasingapi.service;

import com.allane.leasingapi.AbstractIT;
import com.allane.leasingapi.LeasingApiApplication;
import com.allane.leasingapi.dto.Contract;
import com.allane.leasingapi.dto.Contracts;
import com.allane.leasingapi.dto.cruddto.CrudContractDto;
import com.allane.leasingapi.exception.BadRequestException;
import com.allane.leasingapi.exception.NotFoundException;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = LeasingApiApplication.class)
@ActiveProfiles("test")
class ContractServiceTest extends AbstractIT {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @SpyBean
    private ContractService classToTest;

    @BeforeEach
    void cleanUp() {
        contractRepository.deleteAll();
        vehicleRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void create_CustomerNotFound() {

        //Arrange
        CrudContractDto input = getContractDTO();

        //Act & Assert
        assertThatThrownBy(() -> classToTest.create(input))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not Found with Customer ID: 1001");
    }

    @Test
    void create_VehicleNotFound() {

        //Arrange
        CrudContractDto input = getContractDTO();
        input.setCustomerId(null);

        //Act & Assert
        assertThatThrownBy(() -> classToTest.create(input))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not Found with Vehicle ID: 20007");
    }

    @Test
    void create() {

        //Arrange
        CrudContractDto input = getContractDTO();

        VehicleEntity savedVehicle = vehicleRepository.save(getVehicleEntity());
        CustomerEntity savedCustomer = customerRepository.save(getCustomerEntity());

        when(spy(classToTest).findCustomerEntity(input.getCustomerId())).thenReturn(Optional.of(savedCustomer));
        when(spy(classToTest).findVehicleEntity(input.getVehicleId())).thenReturn(Optional.of(savedVehicle));

        //Act
        Contract contract = classToTest.create(input);

        //Assert
        assertThat(contract.getMonthlyRate()).isEqualTo(246.75);
        assertThat(contract.getValidFrom()).isEqualTo("2023-05-01");
        assertThat(contract.getValidUntil()).isEqualTo("2023-06-02");

        assertThat(contract.getCustomer().getCustomerId()).isEqualTo(savedCustomer.getId());
        assertThat(contract.getCustomer().getFirstName()).isEqualTo("SOMETHING");
        assertThat(contract.getCustomer().getLastName()).isEqualTo("NOTHING");
        assertThat(contract.getCustomer().getBirthDate()).isEqualTo("2023-06-02");

        assertThat(contract.getVehicle().getVehicleId()).isEqualTo(savedVehicle.getId());
        assertThat(contract.getVehicle().getBrand()).isEqualTo("ANYTHING");
        assertThat(contract.getVehicle().getModel()).isEqualTo("SOMETHING");
        assertThat(contract.getVehicle().getModelYear()).isEqualTo(2006);
        assertThat(contract.getVehicle().getPrice()).isEqualTo(0.0);
        assertThat(contract.getVehicle().getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
    }

    @Test
    void create_throwsBadRequest() {

        //Arrange
        CrudContractDto input = getContractDTO();
        input.setValidFrom(Date.valueOf("2023-06-01"));
        input.setValidUntil(Date.valueOf("2023-05-02"));

        //Act & Assert
        assertThatThrownBy(() -> classToTest.create(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Valid Until Date Should be Greater than Valid From Date");
    }

    @Test
    void create_withNULL() {

        //Arrange
        CrudContractDto input = getContractDTO();

        input.setCustomerId(null);
        input.setVehicleId(null);

        //Act
        Contract contract = classToTest.create(input);

        //Assert
        assertThat(contract.getMonthlyRate()).isEqualTo(246.75);
        assertThat(contract.getValidFrom()).isEqualTo("2023-05-01");
        assertThat(contract.getValidUntil()).isEqualTo("2023-06-02");
        assertThat(contract.getCustomer()).isNull();
        assertThat(contract.getVehicle()).isNull();
    }

    @Test
    void find() {

        //Arrange
        CrudContractDto input = getContractDTO();
        input.setCustomerId(null);
        input.setVehicleId(null);

        //Act
        Contract create = classToTest.create(input);
        Contract find = classToTest.find(create.getContractNumber());

        //Assert
        assertThat(find.getMonthlyRate()).isEqualTo(246.75);
        assertThat(find.getValidFrom()).isEqualTo("2023-05-01");
        assertThat(find.getValidUntil()).isEqualTo("2023-06-02");
        assertThat(find.getCustomer()).isNull();
        assertThat(find.getVehicle()).isNull();
        assertThat(find.getContractNumber()).isEqualTo(create.getContractNumber());
    }

    @Test
    void findAll() {

        //Arrange
        CrudContractDto input = getContractDTO();
        input.setCustomerId(null);
        input.setVehicleId(null);

        //Act
        Contract create = classToTest.create(input);
        Contracts findAll = classToTest.findAll();

        //Assert
        assertThat(findAll.getContractList().get(0).getMonthlyRate()).isEqualTo(246.75);
        assertThat(findAll.getContractList().get(0).getValidFrom()).isEqualTo("2023-05-01");
        assertThat(findAll.getContractList().get(0).getValidUntil()).isEqualTo("2023-06-02");
        assertThat(findAll.getContractList().get(0).getCustomer()).isNull();
        assertThat(findAll.getContractList().get(0).getVehicle()).isNull();
        assertThat(findAll.getContractList().get(0).getContractNumber()).isEqualTo(create.getContractNumber());
    }

    @Test
    void findAll_EmptyList() {

        //Act
        Contracts findAll = classToTest.findAll();

        //Assert
        assertThat(findAll.getContractList()).isEmpty();
    }

    @Test
    void find_NotFoundContract() {

        //Act & Assert
        assertThatThrownBy(() -> classToTest.find(3000000L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not Found with Contract ID: 3000000");
    }

    @Test
    void delete() {

        //Arrange
        CrudContractDto input = getContractDTO();

        VehicleEntity savedVehicle = vehicleRepository.save(getVehicleEntity());
        CustomerEntity savedCustomer = customerRepository.save(getCustomerEntity());

        when(spy(classToTest).findCustomerEntity(input.getCustomerId())).thenReturn(Optional.of(savedCustomer));
        when(spy(classToTest).findVehicleEntity(input.getVehicleId())).thenReturn(Optional.of(savedVehicle));

        //Act
        Contract contract = classToTest.create(input);
        Long contractNumber = contract.getContractNumber();

        //Assert
        assertThat(contract.getMonthlyRate()).isEqualTo(246.75);
        assertThat(contract.getValidFrom()).isEqualTo("2023-05-01");
        assertThat(contract.getValidUntil()).isEqualTo("2023-06-02");

        assertThat(contract.getCustomer().getCustomerId()).isEqualTo(savedCustomer.getId());
        assertThat(contract.getCustomer().getFirstName()).isEqualTo("SOMETHING");
        assertThat(contract.getCustomer().getLastName()).isEqualTo("NOTHING");
        assertThat(contract.getCustomer().getBirthDate()).isEqualTo("2023-06-02");

        assertThat(contract.getVehicle().getVehicleId()).isEqualTo(savedVehicle.getId());
        assertThat(contract.getVehicle().getBrand()).isEqualTo("ANYTHING");
        assertThat(contract.getVehicle().getModel()).isEqualTo("SOMETHING");
        assertThat(contract.getVehicle().getModelYear()).isEqualTo(2006);
        assertThat(contract.getVehicle().getPrice()).isEqualTo(0.0);
        assertThat(contract.getVehicle().getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");

        //Act
        classToTest.delete(contract.getContractNumber());
        Optional<CustomerEntity> customerEntity = customerRepository.findById(savedCustomer.getId());
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(savedVehicle.getId());

        //Assert

        assertThat(customerEntity.get().getFirstName()).isEqualTo("SOMETHING");
        assertThat(customerEntity.get().getLastName()).isEqualTo("NOTHING");
        assertThat(customerEntity.get().getId()).isEqualTo(savedCustomer.getId());
        assertThat(customerEntity.get().getLeasingContractEntities()).isEmpty();

        assertThat(vehicleEntity.get().getId()).isEqualTo(savedVehicle.getId());
        assertThat(vehicleEntity.get().getBrand()).isEqualTo("ANYTHING");
        assertThat(vehicleEntity.get().getModel()).isEqualTo("SOMETHING");
        assertThat(vehicleEntity.get().getModelYear()).isEqualTo(2006);
        assertThat(vehicleEntity.get().getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
        assertThat(vehicleEntity.get().getPrice()).isEqualTo(0.0);
        assertThat(vehicleEntity.get().getContractEntity()).isNull();

        assertThatThrownBy(() -> classToTest.find(contractNumber))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not Found with Contract ID: " + contractNumber);

    }

    @Test
    void Update() {

        //Arrange
        CrudContractDto input = getContractDTO();

        VehicleEntity savedVehicle = vehicleRepository.save(getVehicleEntity());
        CustomerEntity savedCustomer = customerRepository.save(getCustomerEntity());

        when(spy(classToTest).findCustomerEntity(input.getCustomerId())).thenReturn(Optional.of(savedCustomer));
        when(spy(classToTest).findVehicleEntity(input.getVehicleId())).thenReturn(Optional.of(savedVehicle));

        //Act
        Contract contract = classToTest.create(input);

        //Assert
        assertThat(contract.getMonthlyRate()).isEqualTo(246.75);
        assertThat(contract.getValidFrom()).isEqualTo("2023-05-01");
        assertThat(contract.getValidUntil()).isEqualTo("2023-06-02");

        assertThat(contract.getCustomer().getCustomerId()).isEqualTo(savedCustomer.getId());
        assertThat(contract.getCustomer().getFirstName()).isEqualTo("SOMETHING");
        assertThat(contract.getCustomer().getLastName()).isEqualTo("NOTHING");
        assertThat(contract.getCustomer().getBirthDate()).isEqualTo("2023-06-02");

        assertThat(contract.getVehicle().getVehicleId()).isEqualTo(savedVehicle.getId());
        assertThat(contract.getVehicle().getBrand()).isEqualTo("ANYTHING");
        assertThat(contract.getVehicle().getModel()).isEqualTo("SOMETHING");
        assertThat(contract.getVehicle().getModelYear()).isEqualTo(2006);
        assertThat(contract.getVehicle().getPrice()).isEqualTo(0.0);
        assertThat(contract.getVehicle().getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");

        // Arrange
        CrudContractDto crudContractDto = getCrudContractDto();

        VehicleEntity vehicleEntityToUpdate = getVehicleEntity();
        vehicleEntityToUpdate.setId(null);
        vehicleEntityToUpdate.setVehicleIdentificationNumber("NOTHING");

        CustomerEntity customerEntityToUpdate = getCustomerEntity();
        customerEntityToUpdate.setId(null);

        VehicleEntity updatedVehicle = vehicleRepository.save(vehicleEntityToUpdate);
        CustomerEntity updatedCustomer = customerRepository.save(customerEntityToUpdate);

        when(spy(classToTest).findCustomerEntity(customerEntityToUpdate.getId())).thenReturn(Optional.of(updatedCustomer));
        when(spy(classToTest).findVehicleEntity(updatedVehicle.getId())).thenReturn(Optional.of(updatedVehicle));

        crudContractDto.setCustomerId(updatedCustomer.getId());
        crudContractDto.setVehicleId(updatedVehicle.getId());

        //Act
        Contract update = classToTest.update(contract.getContractNumber(), crudContractDto);

        //Assert
        assertThat(update.getMonthlyRate()).isEqualTo(500.89);
        assertThat(update.getValidFrom()).isEqualTo("2022-12-31");
        assertThat(update.getValidUntil()).isEqualTo("2026-12-30");
        assertThat(update.getVehicle().getVehicleId()).isEqualTo(updatedVehicle.getId());
        assertThat(update.getCustomer().getCustomerId()).isEqualTo(updatedCustomer.getId());
    }

    @Test
    void Update_withNull() {

        //Arrange
        CrudContractDto input = getContractDTO();

        VehicleEntity savedVehicle = vehicleRepository.save(getVehicleEntity());
        CustomerEntity savedCustomer = customerRepository.save(getCustomerEntity());

        when(spy(classToTest).findCustomerEntity(input.getCustomerId())).thenReturn(Optional.of(savedCustomer));
        when(spy(classToTest).findVehicleEntity(input.getVehicleId())).thenReturn(Optional.of(savedVehicle));

        //Act
        Contract contract = classToTest.create(input);

        //Assert
        assertThat(contract.getMonthlyRate()).isEqualTo(246.75);
        assertThat(contract.getValidFrom()).isEqualTo("2023-05-01");
        assertThat(contract.getValidUntil()).isEqualTo("2023-06-02");

        assertThat(contract.getCustomer().getCustomerId()).isEqualTo(savedCustomer.getId());
        assertThat(contract.getCustomer().getFirstName()).isEqualTo("SOMETHING");
        assertThat(contract.getCustomer().getLastName()).isEqualTo("NOTHING");
        assertThat(contract.getCustomer().getBirthDate()).isEqualTo("2023-06-02");

        assertThat(contract.getVehicle().getVehicleId()).isEqualTo(savedVehicle.getId());
        assertThat(contract.getVehicle().getBrand()).isEqualTo("ANYTHING");
        assertThat(contract.getVehicle().getModel()).isEqualTo("SOMETHING");
        assertThat(contract.getVehicle().getModelYear()).isEqualTo(2006);
        assertThat(contract.getVehicle().getPrice()).isEqualTo(0.0);
        assertThat(contract.getVehicle().getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");

        //Arrange
        CrudContractDto crudContractDto = getCrudContractDto();

        crudContractDto.setCustomerId(null);
        crudContractDto.setVehicleId(null);

        //Act
        Contract update = classToTest.update(contract.getContractNumber(), crudContractDto);

        //Assert
        assertThat(update.getMonthlyRate()).isEqualTo(500.89);
        assertThat(update.getValidFrom()).isEqualTo("2022-12-31");
        assertThat(update.getValidUntil()).isEqualTo("2026-12-30");
        assertThat(update.getVehicle()).isNull();
        assertThat(update.getCustomer()).isNull();
    }

    @Test
    void update_NotFound() {

        //Arrange
        CrudContractDto crudContractDto = getCrudContractDto();

        //Act & Assert
        assertThatThrownBy(() -> classToTest.update(3000000000L, crudContractDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not Found with Contract ID: 3000000000");
    }

    @Test
    void update_throwsBadRequest() {

        //Arrange
        CrudContractDto input = getContractDTO();
        input.setVehicleId(null);
        input.setCustomerId(null);

        Contract contract = classToTest.create(input);

        CrudContractDto crudContractDto = getCrudContractDto();
        crudContractDto.setValidFrom(Date.valueOf("2023-06-01"));
        crudContractDto.setValidUntil(Date.valueOf("2023-05-02"));

        Long contractNumber = contract.getContractNumber();

        //Act & Assert
        assertThatThrownBy(() -> classToTest.update(contractNumber, crudContractDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Valid Until Date Should be Greater than Valid From Date");
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
}
