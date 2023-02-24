package com.allane.leasingapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Contract {

    @JsonProperty("contract_number")
    private Long contractNumber;

    @JsonProperty("monthly_rate")
    private double monthlyRate;

    @JsonProperty("valid_from")
    private String validFrom;

    @JsonProperty("valid_until")
    private String validUntil;

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("vehicle")
    private Vehicle vehicle;

    @Getter
    @Setter
    @Builder
    public static class Vehicle {

        @JsonProperty("vehicle_id")
        private Long vehicleId;

        @JsonProperty("brand")
        private String brand;

        @JsonProperty("model")
        private String model;

        @JsonProperty("model_year")
        private Integer modelYear;

        @JsonProperty("vehicle_identification_number")
        private String vehicleIdentificationNumber;
    }

    @Getter
    @Setter
    @Builder
    public static class Customer {

        @JsonProperty("customer_id")
        private Long customerId;

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;
    }
}