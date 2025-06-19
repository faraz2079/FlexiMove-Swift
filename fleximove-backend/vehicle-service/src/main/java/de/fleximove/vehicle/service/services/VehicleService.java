package de.fleximove.vehicle.service.services;

import de.fleximove.vehicle.service.clients.BookingServiceClient;
import de.fleximove.vehicle.service.clients.RatingServiceClient;
import de.fleximove.vehicle.service.clients.UserServiceClient;
import de.fleximove.vehicle.service.domain.Vehicle;
import de.fleximove.vehicle.service.domain.valueobject.*;
import de.fleximove.vehicle.service.dto.*;
import de.fleximove.vehicle.service.repository.VehicleRepository;
import de.fleximove.vehicle.service.utils.DistanceUtils;
import de.fleximove.vehicle.service.utils.VehicleMapper;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final GeocodingService geocodingService;
    private final UserServiceClient userServiceClient;
    private final RatingServiceClient ratingServiceClient;
    private final BookingServiceClient bookingServiceClient;

    @Autowired
    VehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper, GeocodingService geocodingService, UserServiceClient userServiceClient, RatingServiceClient ratingServiceClient, BookingServiceClient bookingServiceClient){
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.geocodingService = geocodingService;
        this.userServiceClient = userServiceClient;
        this.ratingServiceClient = ratingServiceClient;
        this.bookingServiceClient = bookingServiceClient;
    }

    public void registerNewVehicle(VehicleRequest request, Long providerId) {
        Vehicle vehicle = vehicleMapper.mapToVehicle(request, providerId);
        vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> fetchVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = fetchVehicleById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle with ID " + id + " not found"));

        if (vehicle.getStatus() == VehicleStatus.IN_USE || vehicle.getStatus() == VehicleStatus.BOOKED) {
            throw new IllegalStateException("Vehicle cannot be deleted while it is in use or booked.");
        }

        ratingServiceClient.deleteRatingsByVehicleId(vehicle.getId());
        bookingServiceClient.deleteBookingsByVehicleId(vehicle.getId());

        vehicleRepository.delete(vehicle);
    }

    @Transactional
    public void deleteAllVehiclesByProviderId(Long providerId) {
        List<Vehicle> vehicles = vehicleRepository.findAllByProviderId(providerId);
        if (vehicles.isEmpty()) {
            throw new EntityNotFoundException("No vehicles found for provider ID: " + providerId);
        }

        boolean hasActiveVehicles = vehicles.stream()
                .anyMatch(v -> v.getStatus() == VehicleStatus.IN_USE || v.getStatus() == VehicleStatus.BOOKED);

        if (hasActiveVehicles) {
            throw new IllegalStateException("Some vehicles are in use or booked. Deletion stopped.");
        }
        for (Vehicle v : vehicles) {
            try {
                ratingServiceClient.deleteRatingsByVehicleId(v.getId());
            } catch (FeignException e) {
                throw new IllegalStateException("Failed to delete ratings for vehicle " + v.getId() + ": " + e.getMessage());
            }
        }

        for (Vehicle v: vehicles) {
            try {
                bookingServiceClient.deleteBookingsByVehicleId(v.getId());
            } catch (FeignException e) {
                throw new IllegalStateException("Failed to delete bookings for vehicle " + v.getId() + ": " + e.getMessage());
            }
        }
        vehicleRepository.deleteAllByProviderId(providerId);
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
            Double avgVehicleRating;
            Double avgProviderRating;
            try {
                avgVehicleRating = ratingServiceClient.getAverageRatingForVehicle(vehicle.getId());
            } catch (Exception e) {
                avgVehicleRating = 0.0;
            }

            try {
                avgProviderRating = ratingServiceClient.getAverageRatingForProvider(vehicle.getProviderId());
            } catch (Exception e) {
                avgProviderRating = 0.0;
            }
            String providerName = userServiceClient.getProviderCompanyName(vehicle.getProviderId());
            String address = geocodingService.reverseGeocode(vehicle.getCurrentLocation());
            return new NearestAvailableVehicleResponse(
                    vehicle.getId(),
                    vehicle.getVehicleModel(),
                    providerName,
                    vehicle.getVehicleType().toString(),
                    vehicle.getStatus().toString(),
                    vehicle.getVehiclePrice().getAmount(),
                    vehicle.getVehiclePrice().getBillingModel().toString(),
                    address,
                    vehicle.getCurrentLocation().getLatitude(),
                    vehicle.getCurrentLocation().getLongitude(),
                    vehicleWithDistance.distanceInKm(),
                    avgVehicleRating,
                    avgProviderRating,
                    vehicle.getRestrictions()
                    );
        }).toList();
    }

    public List<ProviderVehicleResponse> listProviderVehiclesWithRatings(Long providerId) {
        List<Vehicle> vehicles = vehicleRepository.findAllByProviderId(providerId);

        return vehicles.stream().map(vehicle -> {
            Double avgVehicleRating;
            try {
                avgVehicleRating = ratingServiceClient.getAverageRatingForVehicle(vehicle.getId());
            } catch (Exception e) {
                avgVehicleRating = 0.0;
            }
            String address = geocodingService.reverseGeocode(vehicle.getCurrentLocation());
            return new ProviderVehicleResponse(
                    vehicle.getId(),
                    vehicle.getIdentificationNumber().getIdentNumber(),
                    vehicle.getVehicleModel(),
                    vehicle.getVehicleType().toString(),
                    vehicle.getStatus().toString(),
                    vehicle.getVehiclePrice().getAmount(),
                    vehicle.getVehiclePrice().getBillingModel().toString(),
                    address,
                    vehicle.getCurrentLocation().getLatitude(),
                    vehicle.getCurrentLocation().getLongitude(),
                    avgVehicleRating,
                    vehicle.getRestrictions()
            );
        }).toList();
    }


    public void updateVehicleStatus(Long vehicleId, VehicleStatus newStatus) {
        Vehicle vehicle = fetchVehicleById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with ID: " + vehicleId));
        vehicle.setStatus(newStatus);
    }


    public void updateVehicleLocation(Long vehicleId, double latitude, double longitude) {
        Vehicle vehicle = fetchVehicleById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with ID: " + vehicleId));
        vehicle.setCurrentLocation(new Location(latitude, longitude));
        vehicleRepository.save(vehicle);
    }

    public void editVehicleInformation(Long vehicleId, EditVehicleRequest request) {
        Vehicle vehicleToUpdate = fetchVehicleById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with ID: " + vehicleId));

        if (vehicleToUpdate.getStatus() == VehicleStatus.BOOKED || vehicleToUpdate.getStatus() == VehicleStatus.IN_USE) {
            throw new IllegalStateException("Vehicle cannot be updated while it is booked or in use.");
        }

        updateIdentificationNumberIfPresent(vehicleToUpdate, request);
        updateVehicleModelIfPresent(vehicleToUpdate, request);
        updateVehicleTypeIfPresent(vehicleToUpdate, request);
        updateStatusIfPresent(vehicleToUpdate, request);
        updateAddressIfPresent(vehicleToUpdate, request);
        updateRestrictionsIfPresent(vehicleToUpdate, request);
        updatePriceIfPresent(vehicleToUpdate, request);

        validateVehicleConsistency(vehicleToUpdate);

        vehicleRepository.save(vehicleToUpdate);
    }

    private void updateIdentificationNumberIfPresent(Vehicle vehicle, EditVehicleRequest request) {
        if (request.getIdentificationNumber() != null && !request.getIdentificationNumber().equals(vehicle.getIdentificationNumber().getIdentNumber())) {
            String newIdent = request.getIdentificationNumber();
            Optional<Vehicle> existingVehicleWithIdentNumber = vehicleRepository.findByIdentificationNumberIdentNumber(newIdent);
            if (existingVehicleWithIdentNumber.isPresent()) {
                throw new DataIntegrityViolationException("Identification number already in use.");
            }

            vehicle.setIdentificationNumber(new IdentificationNumber(newIdent));
        }
    }


    private void updateVehicleModelIfPresent(Vehicle vehicle, EditVehicleRequest request) {
        if (request.getVehicleModel() != null) {
            vehicle.setVehicleModel(request.getVehicleModel());
        }
    }

    private void updateVehicleTypeIfPresent(Vehicle vehicle, EditVehicleRequest request) {
        if (request.getVehicleType() != null) {
            VehicleType type = VehicleType.valueOf(request.getVehicleType().toUpperCase());
            vehicle.setVehicleType(type);
        }
    }

    private void updateStatusIfPresent(Vehicle vehicle, EditVehicleRequest request) {
        if (request.getStatus() != null) {
            VehicleStatus status = VehicleStatus.valueOf(request.getStatus().toUpperCase());
            vehicle.setStatus(status);
        }
    }

    private void updatePriceIfPresent(Vehicle vehicle, EditVehicleRequest request) {
        BillingModel billingModel = BillingModel.valueOf(request.getBillingModel().toUpperCase());
        vehicle.setVehiclePrice(new Price(request.getPriceAmount(), billingModel));
    }

    private void updateAddressIfPresent(Vehicle vehicle, EditVehicleRequest request) {
        if (request.getAddress() != null && !request.getAddress().isBlank()) {
            Location location = geocodingService.geocodeAddress(request.getAddress());
            vehicle.setCurrentLocation(location);
        }
    }


    private void updateRestrictionsIfPresent(Vehicle vehicle, EditVehicleRequest request) {
        VehicleRestrictions restrictions = vehicle.getRestrictions();

        if (request.getMinAge() != null) { restrictions.setMinAge(request.getMinAge()); }
        if (request.getMaxBookingTimeMinutes() != null) { restrictions.setMaxBookingTimeMinutes(request.getMaxBookingTimeMinutes()); }
        if (request.getMaxDistanceKm() != null) { restrictions.setMaxDistanceKm(request.getMaxDistanceKm()); }
        if (request.getMaxPassengers() != null) { restrictions.setMaxPassengers(request.getMaxPassengers()); }
        if (request.getRequiredLicenseType() != null) {
            restrictions.setRequiredLicense(
                    DriverLicenseType.licenseTypeFromCode(request.getRequiredLicenseType())
            );
        }

        vehicle.setRestrictions(restrictions);
    }

    private void validateVehicleConsistency(Vehicle vehicle) {
        VehicleRestrictions vehicleRestrictions = vehicle.getRestrictions();
        if (vehicle.getVehicleType().requiresLicense()) {
            DriverLicenseType license = vehicleRestrictions.getRequiredLicense();
            if (license == null || license == DriverLicenseType.NONE) {
                throw new IllegalStateException("Vehicle type '" + vehicle.getVehicleType() + "' requires a valid license, but 'NONE' was provided.");
            }
        }
        if(vehicle.getVehicleType().requiresMinAge() && vehicleRestrictions.getMinAge() == null) {
            throw new IllegalArgumentException("Minimum age is required for vehicle type: " + vehicle.getVehicleType());
        }
    }
}