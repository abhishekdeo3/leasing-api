package com.allane.leasingapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
public class Customer {

    @JsonProperty("customer_id")
    private Long customerId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("birthdate")
    private Date birthdate;

    @JsonProperty("contracts")
    private Set<Contract> contractList;

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
