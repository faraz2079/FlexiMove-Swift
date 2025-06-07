package com.instantmobility.booking.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
public class Trip {
    @Transient
    private  List<GeoLocation> route;
    private double distance;
    private String trip_status;
    private UUID id = UUID.randomUUID();

    public Trip(UUID id) {
        this.id = id;
        this.route = new ArrayList<>();
        this.distance = 0.0;
        this.trip_status = "IN_PROGRESS";
    }

    public void recordLocation(GeoLocation location) {
        if (this.route == null) {
            this.route = new ArrayList<>();
        }
        if (!"IN_PROGRESS".equals(trip_status)) {
            throw new IllegalStateException("Cannot record location when trip is not in progress");
        }

        route.add(location);

        // Calculate distance if there are at least two points
        if (route.size() >= 2) {
            GeoLocation previousLocation = route.get(route.size() - 2);
            double segmentDistance = calculateDistance(previousLocation, location);
            distance += segmentDistance;
        }
    }

    public void complete() {
        if (!"IN_PROGRESS".equals(trip_status)) {
            throw new IllegalStateException("Cannot complete a trip that is not in progress");
        }
        trip_status = "COMPLETED";
    }

    private double calculateDistance(GeoLocation from, GeoLocation to) {
        if (this.route == null) {
            this.route = new ArrayList<>();
        }
        // Simple Haversine formula for distance calculation
        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(to.getLatitude() - from.getLatitude());
        double lonDistance = Math.toRadians(to.getLongitude() - from.getLongitude());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(from.getLatitude())) * Math.cos(Math.toRadians(to.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in km
    }

    public UUID getId() {
        return id;
    }

    public List<GeoLocation> getRoute() {
        return Collections.unmodifiableList(route);
    }

    public double getDistance() {
        return distance;
    }

    public String getStatus() {
        return trip_status;
    }
}
