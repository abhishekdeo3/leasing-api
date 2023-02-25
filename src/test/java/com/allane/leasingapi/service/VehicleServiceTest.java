package com.allane.leasingapi.service;

import com.allane.leasingapi.AbstractIT;
import com.allane.leasingapi.LeasingApiApplication;
import com.allane.leasingapi.dto.Vehicle;
import com.allane.leasingapi.dto.Vehicles;
import com.allane.leasingapi.dto.cruddto.CrudContractDto;
import com.allane.leasingapi.dto.cruddto.CrudVehicleDto;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(classes = LeasingApiApplication.class)
@ActiveProfiles("test")
class VehicleServiceTest extends AbstractIT {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @SpyBean
    private VehicleService classToTest;

    @BeforeEach
    void cleanUp() {
        contractRepository.deleteAll();
        vehicleRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void create() {

        //Arrange
        CrudVehicleDto input = getVehicleDTO();

        //Act
        Vehicle vehicle = classToTest.create(input);

        //Assert;
        assertThat(vehicle.getBrand()).isEqualTo("ANYTHING");
        assertThat(vehicle.getModel()).isEqualTo("SOMETHING");
        assertThat(vehicle.getPrice()).isEqualTo(0.0);
        assertThat(vehicle.getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
        assertThat(vehicle.getYear()).isEqualTo(2006);
        assertThat(vehicle.getContract()).isNull();
    }

    @Test
    void create_withDuplicateVehicleIdNumber() {

        //Arrange
        CrudVehicleDto input = getVehicleDTO();

        //Act
        Vehicle vehicle = classToTest.create(input);

        //Assert;
        assertThat(vehicle.getBrand()).isEqualTo("ANYTHING");
        assertThat(vehicle.getModel()).isEqualTo("SOMETHING");
        assertThat(vehicle.getPrice()).isEqualTo(0.0);
        assertThat(vehicle.getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
        assertThat(vehicle.getYear()).isEqualTo(2006);
        assertThat(vehicle.getContract()).isNull();

        //Act & Assert
        assertThatThrownBy(() -> classToTest.create(input))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void find() {

        //Arrange
        CrudVehicleDto input = getVehicleDTO();

        //Act
        Vehicle vehicle = classToTest.create(input);
        Vehicle find = classToTest.find(vehicle.getVehicleId());

        //Assert
        assertThat(vehicle.getBrand()).isEqualTo("ANYTHING");
        assertThat(vehicle.getModel()).isEqualTo("SOMETHING");
        assertThat(vehicle.getPrice()).isEqualTo(0.0);
        assertThat(vehicle.getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
        assertThat(vehicle.getYear()).isEqualTo(2006);
        assertThat(vehicle.getContract()).isNull();
    }

    @Test
    void find_withContract() {

        //Arrange
        CrudVehicleDto input = getVehicleDTO();

        //Act
        Vehicle vehicle = classToTest.create(input);
        Vehicle find = classToTest.find(vehicle.getVehicleId());

        //Assert
        assertThat(find.getBrand()).isEqualTo("ANYTHING");
        assertThat(find.getModel()).isEqualTo("SOMETHING");
        assertThat(find.getPrice()).isEqualTo(0.0);
        assertThat(find.getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
        assertThat(find.getYear()).isEqualTo(2006);
        assertThat(find.getContract()).isNull();

        //Arrange
        CrudContractDto contractDTO = getContractDTO();
        contractDTO.setVehicleId(find.getVehicleId());

        CustomerEntity savedCustomer = customerRepository.save(getCustomerEntity());
        Optional<VehicleEntity> savedVehicle = vehicleRepository.findById(find.getVehicleId());

        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setMonthlyRate(246.75);
        contractEntity.setVehicleEntity(savedVehicle.get());
        contractEntity.setValidFrom(Date.valueOf("2023-05-01"));
        contractEntity.setValidUntil(Date.valueOf("2023-06-02"));
        contractEntity.setCustomerEntity(savedCustomer);

        ContractEntity contract = contractRepository.save(contractEntity);

        //Act
        Vehicle findAgain = classToTest.find(vehicle.getVehicleId());

        //Assert
        assertThat(findAgain.getBrand()).isEqualTo("ANYTHING");
        assertThat(findAgain.getModel()).isEqualTo("SOMETHING");
        assertThat(findAgain.getPrice()).isEqualTo(0.0);
        assertThat(findAgain.getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
        assertThat(findAgain.getYear()).isEqualTo(2006);
        assertThat(findAgain.getContract()).isNotNull();

        Vehicle.Contract againContract = findAgain.getContract();

        assertThat(againContract.getMonthlyRate()).isEqualTo(246.75);
        assertThat(againContract.getContractNumber()).isEqualTo(contract.getId());
    }

    @Test
    void findAll() {

        //Arrange
        CrudVehicleDto input = getVehicleDTO();

        //Act
        Vehicle vehicle = classToTest.create(input);
        Vehicle find = classToTest.find(vehicle.getVehicleId());

        CrudContractDto contractDTO = getContractDTO();
        contractDTO.setVehicleId(find.getVehicleId());

        CustomerEntity savedCustomer = customerRepository.save(getCustomerEntity());
        Optional<VehicleEntity> savedVehicle = vehicleRepository.findById(find.getVehicleId());

        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setMonthlyRate(246.75);
        contractEntity.setVehicleEntity(savedVehicle.get());
        contractEntity.setValidFrom(Date.valueOf("2023-05-01"));
        contractEntity.setValidUntil(Date.valueOf("2023-06-02"));
        contractEntity.setCustomerEntity(savedCustomer);

        ContractEntity contract = contractRepository.save(contractEntity);

        Vehicles findAll = classToTest.findAll();

        //Assert
        assertThat(findAll.getVehicleList().get(0).getBrand()).isEqualTo("ANYTHING");
        assertThat(findAll.getVehicleList().get(0).getModel()).isEqualTo("SOMETHING");
        assertThat(findAll.getVehicleList().get(0).getPrice()).isEqualTo(0.0);
        assertThat(findAll.getVehicleList().get(0).getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
        assertThat(findAll.getVehicleList().get(0).getYear()).isEqualTo(2006);

        assertThat(findAll.getVehicleList().get(0).getContract().getMonthlyRate()).isEqualTo(246.75);
        assertThat(findAll.getVehicleList().get(0).getContract().getContractNumber()).isEqualTo(contract.getId());
    }

    @Test
    void findAll_EmptyList() {

        //Act
        Vehicles findAll = classToTest.findAll();

        //Assert
        assertThat(findAll.getVehicleList()).isEmpty();
    }

    @Test
    void find_NotFoundContract() {

        //Act & Assert
        assertThatThrownBy(() -> classToTest.find(3000000L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not Found with Vehicle ID: 3000000");
    }

    @Test
    void delete() {

        //Arrange
        CrudVehicleDto input = getVehicleDTO();

        //Act
        Vehicle vehicle = classToTest.create(input);
        Vehicle find = classToTest.find(vehicle.getVehicleId());

        Long vehicleId = find.getVehicleId();

        //Assert
        assertThat(find.getBrand()).isEqualTo("ANYTHING");
        assertThat(find.getModel()).isEqualTo("SOMETHING");
        assertThat(find.getPrice()).isEqualTo(0.0);
        assertThat(find.getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
        assertThat(find.getYear()).isEqualTo(2006);
        assertThat(find.getContract()).isNull();

        //Arrange
        CrudContractDto contractDTO = getContractDTO();
        contractDTO.setVehicleId(find.getVehicleId());

        CustomerEntity savedCustomer = customerRepository.save(getCustomerEntity());
        Optional<VehicleEntity> savedVehicle = vehicleRepository.findById(find.getVehicleId());

        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setMonthlyRate(246.75);
        contractEntity.setVehicleEntity(savedVehicle.get());
        contractEntity.setValidFrom(Date.valueOf("2023-05-01"));
        contractEntity.setValidUntil(Date.valueOf("2023-06-02"));
        contractEntity.setCustomerEntity(savedCustomer);

        ContractEntity contract = contractRepository.save(contractEntity);

        //Act
        classToTest.delete(savedVehicle.get().getId());

        Optional<ContractEntity> contractEntity1 = contractRepository.findById(contract.getId());

        Optional<CustomerEntity> customerEntity = customerRepository.findById(savedCustomer.getId());

        //Act & Assert

        assertThat(contractEntity1.get().getVehicleEntity()).isNull();
        assertThat(contractEntity1.get().getMonthlyRate()).isEqualTo(246.75);

        assertThat(customerEntity.get().getId()).isEqualTo(savedCustomer.getId());
        assertThat(customerEntity.get().getFirstName()).isEqualTo("SOMETHING");
        assertThat(customerEntity.get().getLastName()).isEqualTo("NOTHING");
        assertThat(customerEntity.get().getLeasingContractEntities()).hasSize(1);

        List<ContractEntity> list =  customerEntity.get().getLeasingContractEntities().stream().toList();

        assertThat(list.get(0).getMonthlyRate()).isEqualTo(246.75);
        assertThat(list.get(0).getId()).isEqualTo(contract.getId());
        assertThat(list.get(0).getVehicleEntity()).isNull();

        assertThatThrownBy(() -> classToTest.find(vehicleId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not Found with Vehicle ID: " + vehicleId);
    }

    @Test
    void Update() {

        //Arrange
        VehicleEntity savedVehicle = vehicleRepository.save(getVehicleEntity());

        assertThat(savedVehicle.getBrand()).isEqualTo("ANYTHING");
        assertThat(savedVehicle.getModel()).isEqualTo("SOMETHING");
        assertThat(savedVehicle.getPrice()).isEqualTo(0.0);
        assertThat(savedVehicle.getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
        assertThat(savedVehicle.getModelYear()).isEqualTo(2006);

        CrudVehicleDto input = getVehicleDTO();
        input.setBrand("NOTHING");
        input.setModel("EVERYTHING");
        input.setPrice(1.1);
        input.setModelYear(2014);

        //Act
        Vehicle vehicle = classToTest.update(savedVehicle.getId(), input);

        //Assert
        assertThat(vehicle.getBrand()).isEqualTo("NOTHING");
        assertThat(vehicle.getModel()).isEqualTo("EVERYTHING");
        assertThat(vehicle.getPrice()).isEqualTo(1.1);
        assertThat(vehicle.getVehicleIdentificationNumber()).isEqualTo("EVERYTHING");
        assertThat(vehicle.getYear()).isEqualTo(2014);
    }

    @Test
    void update_NotFound() {

        //Arrange
        CrudVehicleDto crudVehicleDto = getVehicleDTO();

        //Act & Assert
        assertThatThrownBy(() -> classToTest.update(3000000000L, crudVehicleDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Not Found with Vehicle ID: 3000000000");
    }

    private CrudVehicleDto getVehicleDTO() {

        CrudVehicleDto crudVehicleDto = new CrudVehicleDto();

        crudVehicleDto.setBrand("ANYTHING");
        crudVehicleDto.setModel("SOMETHING");
        crudVehicleDto.setPrice(0.0);
        crudVehicleDto.setVehicleIdentificationNumber("EVERYTHING");
        crudVehicleDto.setModelYear(2006);

        return crudVehicleDto;
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
