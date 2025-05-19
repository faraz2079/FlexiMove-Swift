package com.instantmobility.booking.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Trip {
    private final UUID id;
    private final List<GeoLocation> route;
    private double distance;
    private String status;

    public Trip(UUID id) {
        this.id = id;
        this.route = new ArrayList<>();
        this.distance = 0.0;
        this.status = "IN_PROGRESS";
    }

    public void recordLocation(GeoLocation location) {
        if (!"IN_PROGRESS".equals(status)) {
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
        if (!"IN_PROGRESS".equals(status)) {
            throw new IllegalStateException("Cannot complete a trip that is not in progress");
        }
        status = "COMPLETED";
    }

    private double calculateDistance(GeoLocation from, GeoLocation to) {
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
        return status;
    }
}
