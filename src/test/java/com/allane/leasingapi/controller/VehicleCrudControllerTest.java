package com.allane.leasingapi.controller;

import com.allane.leasingapi.configuration.SecurityConfig;
import com.allane.leasingapi.dto.Vehicle;
import com.allane.leasingapi.dto.Vehicles;
import com.allane.leasingapi.exception.NotFoundException;
import com.allane.leasingapi.service.VehicleService;
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

@WebMvcTest(value = VehicleCrudController.class)
@Import(SecurityConfig.class)
class VehicleCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Test
    void getByVehicleId_return401() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/{vehicle-id}", Integer.toString(1)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void getByCustomerId_return404() throws Exception {

        Mockito.when(vehicleService.find(1L))
                .thenThrow(new NotFoundException("Vehicle", 1));

        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/{vehicle-id}", Integer.toString(1)))
                .andExpect(status().isNotFound()).andExpect(content().string("{\"status\":404," +
                        "\"title\":\"Not Found\",\"detail\":\"Not Found with Vehicle ID: 1\"}"));
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void find() throws Exception {

        //Arrange
        Vehicle vehicle = Vehicle.builder()
                .vehicleId(1L)
                .brand("SOMETHING")
                .model("NOTHING")
                .year(1200)
                .vehicleIdentificationNumber("EVERYTHING")
                .contract(null)
                .price(0.0)
                .build();

        Mockito.when(vehicleService.find(any())).thenReturn(vehicle);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/{vehicle-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"vehicle_id\":1,\"brand\":\"SOMETHING\",\"model\":\"NOTHING\"," +
                        "\"model_year\":1200,\"vehicle_identification_number\":\"EVERYTHING\",\"price\":0.0,\"contract\":null}"));
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void findAll() throws Exception {

        //Arrange
        Vehicle vehicle = Vehicle.builder()
                .vehicleId(1L)
                .brand("SOMETHING")
                .model("NOTHING")
                .year(1200)
                .vehicleIdentificationNumber("EVERYTHING")
                .contract(null)
                .price(0.0)
                .build();

        Vehicles vehicles = new Vehicles();
        vehicles.setVehicleList(List.of(vehicle));

        Mockito.when(vehicleService.findAll()).thenReturn(vehicles);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"vehicles\":[{\"vehicle_id\":1,\"brand\":\"SOMETHING\"," +
                        "\"model\":\"NOTHING\",\"model_year\":1200,\"vehicle_identification_number\":\"EVERYTHING\"," +
                        "\"price\":0.0,\"contract\":null}]}"));
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void create() throws Exception {

        //Arrange
        Vehicle vehicle = Vehicle.builder()
                .vehicleId(1L)
                .brand("SOMETHING")
                .model("NOTHING")
                .year(1200)
                .vehicleIdentificationNumber("EVERYTHING")
                .contract(null)
                .price(0.0)
                .build();

        Mockito.when(vehicleService.create(any())).thenReturn(vehicle);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customer_id\":1,\"first_name\":\"NOTHING\",\"last_name\":\"SOMETHING\"," +
                                "\"birthdate\":\"1200-09-01\",\"contracts\":null}")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"vehicle_id\":1,\"brand\":\"SOMETHING\"," +
                        "\"model\":\"NOTHING\",\"model_year\":1200,\"vehicle_identification_number\":\"EVERYTHING\"," +
                        "\"price\":0.0,\"contract\":null}"));
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void delete() throws Exception {

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/vehicle/{vehicle-id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "allane", password = "allane", roles = "USER")
    void update() throws Exception {

        //Arrange
        Vehicle vehicle = Vehicle.builder()
                .vehicleId(1L)
                .brand("SOMETHING")
                .model("NOTHING")
                .year(1200)
                .vehicleIdentificationNumber("EVERYTHING")
                .contract(null)
                .price(0.0)
                .build();

        Mockito.when(vehicleService.update(anyLong(), any())).thenReturn(vehicle);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/vehicle/{vehicle-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customer_id\":1,\"first_name\":\"NOTHING\"," +
                                "\"last_name\":\"SOMETHING\",\"birthdate\":\"1200-09-01\",\"contracts\":null}")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"vehicle_id\":1,\"brand\":\"SOMETHING\",\"model\":\"NOTHING\"," +
                        "\"model_year\":1200,\"vehicle_identification_number\":\"EVERYTHING\",\"price\":0.0,\"contract\":null}"));
    }
}
