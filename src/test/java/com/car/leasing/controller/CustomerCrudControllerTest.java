package com.car.leasing.controller;

import com.car.leasing.dto.Customer;
import com.car.leasing.dto.Customers;
import com.car.leasing.exception.NotFoundException;
import com.car.leasing.service.CustomerService;
import com.car.leasing.configuration.SecurityConfig;
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

@WebMvcTest(value = CustomerCrudController.class)
@Import(SecurityConfig.class)
class CustomerCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void getByCustomerId_return401() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/customer/{customer-id}", Integer.toString(1)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void getByCustomerId_return404() throws Exception {

        Mockito.when(customerService.find(1L))
                .thenThrow(new NotFoundException("Customer", 1));

        mockMvc.perform(MockMvcRequestBuilders.get("/customer/{customer-id}", Integer.toString(1)))
                .andExpect(status().isNotFound()).andExpect(content().string("{\"status\":404," +
                        "\"title\":\"Not Found\",\"detail\":\"Not Found with Customer ID: 1\"}"));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void find() throws Exception {

        //Arrange
        Customer customer = Customer.builder()
                .customerId(1L)
                .lastName("SOMETHING")
                .firstName("NOTHING")
                .birthdate("1200-09-01")
                .contractList(null)
                .build();

        Mockito.when(customerService.find(any())).thenReturn(customer);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/customer/{customer-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"customer_id\":1,\"first_name\":\"NOTHING\"," +
                        "\"last_name\":\"SOMETHING\",\"birthdate\":\"1200-09-01\",\"contracts\":null}"));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void findAll() throws Exception {

        //Arrange
        Customer customer = Customer.builder()
                .customerId(1L)
                .lastName("SOMETHING")
                .firstName("NOTHING")
                .birthdate("1200-09-01")
                .contractList(null)
                .build();

        Customers customers = new Customers();
        customers.setCustomerList(List.of(customer));

        Mockito.when(customerService.findAll()).thenReturn(customers);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"customers\":[{\"customer_id\":1," +
                        "\"first_name\":\"NOTHING\",\"last_name\":\"SOMETHING\",\"birthdate\":\"1200-09-01\",\"contracts\":null}]}"));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void create() throws Exception {

        //Arrange
        Customer customer = Customer.builder()
                .customerId(1L)
                .lastName("SOMETHING")
                .firstName("NOTHING")
                .birthdate("1200-09-01")
                .contractList(null)
                .build();

        Mockito.when(customerService.create(any())).thenReturn(customer);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customer_id\":1,\"first_name\":\"NOTHING\",\"last_name\":\"SOMETHING\"," +
                                "\"birthdate\":\"1200-09-01\",\"contracts\":null}")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"customer_id\":1,\"first_name\":\"NOTHING\"," +
                        "\"last_name\":\"SOMETHING\",\"birthdate\":\"1200-09-01\",\"contracts\":null}"));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void delete() throws Exception {

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/customer/{customer-id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void update() throws Exception {

        //Arrange
        Customer customer = Customer.builder()
                .customerId(1L)
                .lastName("SOMETHING")
                .firstName("NOTHING")
                .birthdate("1200-09-01")
                .contractList(null)
                .build();

        Mockito.when(customerService.update(anyLong(), any())).thenReturn(customer);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/customer/{customer-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customer_id\":1,\"first_name\":\"NOTHING\"," +
                                "\"last_name\":\"SOMETHING\",\"birthdate\":\"1200-09-01\",\"contracts\":null}")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"customer_id\":1,\"first_name\":\"NOTHING\"," +
                        "\"last_name\":\"SOMETHING\",\"birthdate\":\"1200-09-01\",\"contracts\":null}"));
    }
}
