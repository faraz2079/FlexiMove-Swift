package de.fleximove.vehicle.service.services;

import de.fleximove.vehicle.service.domain.Vehicle;
import de.fleximove.vehicle.service.domain.valueobject.Location;
import de.fleximove.vehicle.service.domain.valueobject.VehicleStatus;
import de.fleximove.vehicle.service.dto.NearestAvailableVehicleResponse;
import de.fleximove.vehicle.service.dto.VehicleRequest;
import de.fleximove.vehicle.service.dto.VehicleWithDistance;
import de.fleximove.vehicle.service.repository.VehicleRepository;
import de.fleximove.vehicle.service.utils.DistanceUtils;
import de.fleximove.vehicle.service.utils.VehicleMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final GeocodingService geocodingService;

    @Autowired
    VehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper, GeocodingService geocodingService){
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.geocodingService = geocodingService;
    }

    public void registerNewVehicle(VehicleRequest request, Long providerId) {
        Vehicle vehicle = vehicleMapper.mapToVehicle(request, providerId);
        vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> fetchVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with ID " + id + " not found"));

        vehicleRepository.delete(vehicle);
    }

    public List<NearestAvailableVehicleResponse> findAvailableNearbyVehicles(Location neededLocationWithAvailableVehicles, double radiusInKm) {
        Stream<VehicleWithDistance> foundAvailableVehicles = vehicleRepository.findByStatus(VehicleStatus.AVAILABLE).stream()
                .map(vehicle -> {
                    Location vahicleLocation = vehicle.getCurrentLocation();
                    double distance = DistanceUtils.haversineDistance(
                            neededLocationWithAvailableVehicles.getLatitude(), neededLocationWithAvailableVehicles.getLongitude(),
                            vahicleLocation.getLatitude(), vahicleLocation.getLongitude()
                    );
                    return new VehicleWithDistance(vehicle, distance);
                });

        return foundAvailableVehicles.filter(vehicleWithDistance -> vehicleWithDistance.distanceInKm() <= radiusInKm).map(vehicleWithDistance -> {
            Vehicle vehicle = vehicleWithDistance.vehicle();
            //TODO: get provider name and average ration for current vehicle
            //Double averageRatingForVehicle = ratingServiceClient.getAverageRatingForVehicle(vehicle.getId());
            //String providerName = userServiceClient.getProviderName(vehicle.getProviderId());
            String address = geocodingService.reverseGeocode(vehicle.getCurrentLocation());
            return new NearestAvailableVehicleResponse(
                    vehicle.getId(),
                    vehicle.getVehicleModel(),
                    "Bolt",
                    vehicle.getVehicleType().toString(),
                    vehicle.getStatus().toString(),
                    vehicle.getVehiclePrice().getAmount(),
                    vehicle.getVehiclePrice().getBillingModel().toString(),
                    address,
                    vehicle.getCurrentLocation().getLatitude(),
                    vehicle.getCurrentLocation().getLongitude(),
                    vehicleWithDistance.distanceInKm(),
                    4.5
                    );
        }).toList();
    }
}
