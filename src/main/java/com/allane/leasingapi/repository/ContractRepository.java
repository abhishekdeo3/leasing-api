package com.allane.leasingapi.repository;

import com.allane.leasingapi.model.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

    @Query("SELECT contractEntity FROM ContractEntity contractEntity " +
            "JOIN FETCH CustomerEntity customerEntity " +
            "WHERE customerEntity.id = :customerId")
    Set<ContractEntity> findByCustomerId(Long customerId);

    @Query("SELECT contractEntity FROM ContractEntity contractEntity " +
            "JOIN FETCH VehicleEntity vehicleEntity " +
            "WHERE vehicleEntity.id = :vehicleId")
    ContractEntity findByVehicleId(Long vehicleId);
}
