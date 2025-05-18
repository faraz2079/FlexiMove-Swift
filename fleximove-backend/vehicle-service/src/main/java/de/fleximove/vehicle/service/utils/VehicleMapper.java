package de.fleximove.vehicle.service.utils;

import de.fleximove.vehicle.service.domain.Vehicle;
import de.fleximove.vehicle.service.domain.valueobject.*;
import de.fleximove.vehicle.service.dto.VehicleRequest;
import de.fleximove.vehicle.service.services.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class VehicleMapper {

    GeocodingService geocodingService;

    @Autowired
    VehicleMapper(GeocodingService geocodingService){
        this.geocodingService = geocodingService;
    }

    public Vehicle mapToVehicle(VehicleRequest request, Long providerId) {
        VehicleType type = VehicleType.valueOf(request.getVehicleType().toUpperCase());
        BillingModel billingModel = BillingModel.valueOf(request.getBillingModel().toUpperCase());
        DriverLicenseType licenseType = resolveLicenseType(type, request.getRequiredLicenseType());
        Location currentLocation = resolveAddress(request.getAddress());

        return new Vehicle(
                new IdentificationNumber(request.getIdentificationNumber()),
                request.getVehicleName(),
                type,
                VehicleStatus.AVAILABLE,
                currentLocation,
                new Price(request.getPriceAmount(), billingModel),
                new VehicleRestrictions(
                        request.getMinAge(),
                        request.getMaxBookingTimeMinutes(),
                        request.getMaxDistanceKm(),
                        request.getMaxPassengers(),
                        licenseType
                ),
                providerId
        );
    }

    //TODO: wenn es ein Vehicle mit License ist, dann muss auch minAge angegeben werden
    private static DriverLicenseType resolveLicenseType(VehicleType type, String licenseTypeFromRequest) {
        if (Set.of(VehicleType.MOTORCYCLE, VehicleType.CAR, VehicleType.VAN, VehicleType.TRUCK).contains(type)) {
            return DriverLicenseType.valueOf(licenseTypeFromRequest.toUpperCase());
        }

        return DriverLicenseType.NONE;
    }

    private Location resolveAddress(String address){
        Objects.requireNonNull(address);
        return geocodingService.geocodeAddress(address);
    }
}

