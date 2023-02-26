package com.allane.leasingapi.controller;

import com.allane.leasingapi.configuration.SecurityConfig;
import com.allane.leasingapi.dto.Contract;
import com.allane.leasingapi.dto.Contracts;
import com.allane.leasingapi.exception.NotFoundException;
import com.allane.leasingapi.service.ContractService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ContractCrudController.class)
@Import(SecurityConfig.class)
class ContractCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractService contractService;

    @Test
    void getByContractNumber_return401() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/contract/{contract-number}", Integer.toString(1)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void getByContractNumber_return404() throws Exception {

        Mockito.when(contractService.find(1L))
                .thenThrow(new NotFoundException("Contract", 1));

        mockMvc.perform(MockMvcRequestBuilders.get("/contract/{contract-number}", Integer.toString(1)))
                .andExpect(status().isNotFound()).andExpect(content().string("{\"status\":404," +
                        "\"title\":\"Not Found\",\"detail\":\"Not Found with Contract ID: 1\"}"));
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void find() throws Exception {

        //Arrange
        Contract contract = Contract.builder()
                .contractNumber(1L)
                .monthlyRate(246.75)
                .validFrom("2023-05-01")
                .validUntil("2023-06-02")
                .customer(null)
                .vehicle(null)
                .build();

        Mockito.when(contractService.find(any())).thenReturn(contract);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/contract/{contract-number}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"contract_number\":1,\"monthly_rate\":246.75," +
                        "\"valid_from\":\"2023-05-01\",\"valid_until\":\"2023-06-02\",\"customer\":null,\"vehicle\":null}"));
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void findAll() throws Exception {

        //Arrange
        Contract contract = Contract.builder()
                .contractNumber(1L)
                .monthlyRate(246.75)
                .validFrom("2023-05-01")
                .validUntil("2023-06-02")
                .customer(null)
                .vehicle(null)
                .build();

        Contracts contracts = new Contracts();
        contracts.setContractList(List.of(contract));

        Mockito.when(contractService.findAll()).thenReturn(contracts);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"contracts\":[{\"contract_number\":1," +
                        "\"monthly_rate\":246.75,\"valid_from\":\"2023-05-01\",\"valid_until\":\"2023-06-02\"," +
                        "\"customer\":null,\"vehicle\":null}]}"));
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void create() throws Exception {

        //Arrange
        Contract contract = Contract.builder()
                .contractNumber(1L)
                .monthlyRate(246.75)
                .validFrom("2023-05-01")
                .validUntil("2023-06-02")
                .customer(null)
                .vehicle(null)
                .build();

        Mockito.when(contractService.create(any())).thenReturn(contract);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"monthlyRate\":\"246.75\",\"validFrom\":\"2023-05-01\",\"validUntil\":\"2023-06-02\"," +
                                "\"customerId\":null,\"vehicleId\":null}")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"contract_number\":1,\"monthly_rate\":246.75," +
                        "\"valid_from\":\"2023-05-01\",\"valid_until\":\"2023-06-02\",\"customer\":null,\"vehicle\":null}"));
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void delete() throws Exception {

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/contract/{contract-number}", 1L)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void update() throws Exception {

        //Arrange
        Contract contract = Contract.builder()
                .contractNumber(1L)
                .monthlyRate(246.75)
                .validFrom("2023-05-01")
                .validUntil("2023-06-02")
                .customer(null)
                .vehicle(null)
                .build();

        Mockito.when(contractService.update(anyLong(), any())).thenReturn(contract);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/contract/{contract-number}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"monthlyRate\":\"246.75\",\"validFrom\":\"2023-05-01\",\"validUntil\":\"2023-06-02\"," +
                                "\"customerId\":null,\"vehicleId\":null}")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"contract_number\":1,\"monthly_rate\":246.75," +
                        "\"valid_from\":\"2023-05-01\",\"valid_until\":\"2023-06-02\",\"customer\":null,\"vehicle\":null}"));
    }
}
