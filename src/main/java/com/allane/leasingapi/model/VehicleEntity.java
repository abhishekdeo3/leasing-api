package com.allane.leasingapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "VEHICLE")
public class VehicleEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BRAND")
    private String brand;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "MODEL_YEAR")
    private Integer modelYear;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "VEHICLE_IDENTIFICATION_NUMBER")
    private String vehicleIdentificationNumber;

    @OneToOne(mappedBy = "vehicleEntity", fetch = FetchType.EAGER)
    private ContractEntity contractEntity;
}
