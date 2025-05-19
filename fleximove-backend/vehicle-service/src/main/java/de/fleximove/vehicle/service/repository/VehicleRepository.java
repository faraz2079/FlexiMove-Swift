package de.fleximove.vehicle.service.repository;

import de.fleximove.vehicle.service.domain.Vehicle;
import de.fleximove.vehicle.service.domain.valueobject.VehicleStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    List<Vehicle> findByStatus(VehicleStatus status);
    List<Vehicle> findAllByProviderId(Long providerId);
    Optional<Vehicle> findByIdentificationNumberIdentNumber(String identificationNumber);
    void deleteAllByProviderId(Long providerId);
}
