package de.fleximove.vehicle.service.repository;

import de.fleximove.vehicle.service.domain.Vehicle;
import org.springframework.data.repository.CrudRepository;

public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

}
