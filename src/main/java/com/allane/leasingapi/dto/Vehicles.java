package com.allane.leasingapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Vehicles {

    @JsonProperty("vehicles")
    private List<Vehicle> vehicleList;
}
