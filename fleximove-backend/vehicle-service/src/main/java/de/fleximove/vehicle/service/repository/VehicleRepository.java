package de.fleximove.vehicle.service.repository;

import de.fleximove.vehicle.service.domain.Vehicle;
import de.fleximove.vehicle.service.domain.valueobject.VehicleStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    List<Vehicle> findAll();
    List<Vehicle> findByStatus(VehicleStatus status);
}
