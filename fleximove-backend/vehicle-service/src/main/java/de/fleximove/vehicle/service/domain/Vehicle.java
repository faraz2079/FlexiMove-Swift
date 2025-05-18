package de.fleximove.vehicle.service.domain;

import de.fleximove.vehicle.service.domain.valueobject.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Embedded
    private IdentificationNumber identificationNumber;
    private Long providerId;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    @Enumerated(EnumType.STRING)
    private VehicleStatus status = VehicleStatus.AVAILABLE;
    @Embedded
    private VehicleRestrictions restrictions;
    @Embedded
    private Price vehiclePrice;
    @Embedded
    private Location currentLocation;

    //TODO: validation
    public Vehicle(IdentificationNumber identNumber, VehicleType type, VehicleStatus status, Location currentLocation,
                   Price price,
                   VehicleRestrictions restrictions, Long providerId) {
        this.identificationNumber = Objects.requireNonNull(identNumber, "identNumber must not be null");
        this.vehicleType = Objects.requireNonNull(type, "vehicleType must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.currentLocation = Objects.requireNonNull(currentLocation, "currentLocation must not be null");
        this.vehiclePrice = Objects.requireNonNull(price, "vehiclePrice must not be null");
        this.restrictions = restrictions;
        this.providerId = Objects.requireNonNull(providerId, "providerId must not be null");
    }
}
