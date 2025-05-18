package de.fleximove.vehicle.service.services;

import de.fleximove.vehicle.service.domain.Vehicle;
import de.fleximove.vehicle.service.dto.VehicleRequest;
import de.fleximove.vehicle.service.repository.VehicleRepository;
import de.fleximove.vehicle.service.utils.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private VehicleRepository vehicleRepository;
    private VehicleMapper vehicleMapper;

    @Autowired
    VehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper){
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    public void registerNewVehicle(VehicleRequest request, Long providerId) {
        Vehicle vehicle = vehicleMapper.mapToVehicle(request, providerId);
        vehicleRepository.save(vehicle);
    }

}
