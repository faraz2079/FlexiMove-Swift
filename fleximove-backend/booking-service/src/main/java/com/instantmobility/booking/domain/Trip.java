package com.instantmobility.booking.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@Data
public class Trip {
    @Transient
    private  List<GeoLocation> route;
    private double distance;
    private String trip_status;
    private UUID id = UUID.randomUUID();
    private GeoLocation startLocation;
    private GeoLocation endLocation;
    @Embedded
    private TimeFrame timeFrame;

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
        if (!"IN_PROGRESS".equals(this.trip_status)) {
            throw new IllegalStateException("Cannot record location when trip is not in progress");
        }

        this.route.add(location);

    }

    public void complete() {
        if (!"IN_PROGRESS".equals(this.trip_status)) {
            throw new IllegalStateException("Cannot complete a trip that is not in progress");
        }
        this.trip_status = "COMPLETED";
    }

    public double getDistanceInKm() {
        if (this.startLocation == null || this.endLocation == null) {
            return 0;
        }

        return haversineDistance(
                this.startLocation.getLatitude(), this.startLocation.getLongitude(),
                this.endLocation.getLatitude(), this.endLocation.getLongitude()
        );
    }

    public long getDurationInMinutes() {
        if (this.timeFrame.getStartTime() == null || this.timeFrame.getEndTime() == null) {
            return 0;
        }
        return Duration.between(this.timeFrame.getStartTime(), this.timeFrame.getEndTime()).toMinutes();
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // Earth radius in kilometers
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public List<GeoLocation> getRoute() {
        return Collections.unmodifiableList(route);
    }
}
