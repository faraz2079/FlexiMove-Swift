package de.fleximove.vehicle.service.dto;

import de.fleximove.vehicle.service.domain.Vehicle;

public record VehicleWithDistance(Vehicle vehicle, double distanceInKm) {}

