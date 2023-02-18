package com.allane.leasingapi.dto.cruddto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrudVehicleDto {

    private String brand;

    private String model;

    private Integer modelYear;

    private Double price;

    private String vehicleIdentificationNumber;
}
