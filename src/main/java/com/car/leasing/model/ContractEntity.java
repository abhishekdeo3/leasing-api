package com.car.leasing.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "LEASING_CONTRACT")
public class ContractEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MONTHLY_RATE")
    private double monthlyRate;

    @Column(name = "VALID_FROM")
    private Date validFrom;

    @Column(name = "VALID_UNTIL")
    private Date validUntil;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_ID")
    private CustomerEntity customerEntity;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VEHICLE_ID", referencedColumnName = "ID")
    private VehicleEntity vehicleEntity;
}
