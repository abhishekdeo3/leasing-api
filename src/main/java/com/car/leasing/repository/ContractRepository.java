package com.car.leasing.repository;

import com.car.leasing.model.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

    @Query(" SELECT contractEntity FROM ContractEntity contractEntity " +
            " JOIN FETCH CustomerEntity customerEntity " +
            " ON contractEntity.customerEntity.id = customerEntity.id " +
            " WHERE customerEntity.id = :customerId")
    Set<ContractEntity> findByCustomerId(Long customerId);

    @Query(" SELECT contractEntity FROM ContractEntity contractEntity " +
            " JOIN FETCH VehicleEntity vehicleEntity " +
            " ON contractEntity.vehicleEntity.id = vehicleEntity.id " +
            " WHERE vehicleEntity.id = :vehicleId")
    ContractEntity findByVehicleId(Long vehicleId);
}
