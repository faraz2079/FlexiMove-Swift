package de.fleximove.vehicle.service.domain.valueobject;
import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {

    }
}

