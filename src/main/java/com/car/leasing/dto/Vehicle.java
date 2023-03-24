package com.car.leasing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Vehicle {

    @JsonProperty("vehicle_id")
    private Long vehicleId;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("model")
    private String model;

    @JsonProperty("model_year")
    private Integer year;

    @JsonProperty("vehicle_identification_number")
    private String vehicleIdentificationNumber;

    @JsonProperty("price")
    private double price;

    @JsonProperty("contract")
    private Contract contract;

    @Getter
    @Setter
    @Builder
    public static class Contract {

        @JsonProperty("contract_number")
        private Long contractNumber;

        @JsonProperty("monthly_rate")
        private double monthlyRate;
    }
}
