package com.car.leasing.dto.cruddto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class CrudContractDto {

    private double monthlyRate;

    private Date validFrom;

    private Date validUntil;

    private Long customerId;

    private Long vehicleId;
}
