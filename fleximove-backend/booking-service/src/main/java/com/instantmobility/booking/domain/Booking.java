package com.instantmobility.booking.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "BOOKING")
public class Booking {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private BookingId id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Column(name = "payment_id", columnDefinition = "BINARY(16)")
    private UUID paymentId;

    @Embedded
    private TimeFrame timeFrame;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "pickup_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "pickup_longitude"))
    })
    private GeoLocation pickupLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "dropoff_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "dropoff_longitude"))
    })
    private GeoLocation dropoffLocation;

    @Column(name = "cost")
    private double cost;

    @Embedded
    @AttributeOverrides({
            // Map all Trip fields to prefixed column names
            @AttributeOverride(name = "id", column = @Column(name = "trip_id")),
            @AttributeOverride(name = "startTime", column = @Column(name = "trip_start_time")),
            @AttributeOverride(name = "endTime", column = @Column(name = "trip_end_time")),
            @AttributeOverride(name = "startLatitude", column = @Column(name = "trip_start_latitude")),
            @AttributeOverride(name = "startLongitude", column = @Column(name = "trip_start_longitude")),
            @AttributeOverride(name = "endLatitude", column = @Column(name = "trip_end_latitude")),
            @AttributeOverride(name = "endLongitude", column = @Column(name = "trip_end_longitude")),
            @AttributeOverride(name = "completed", column = @Column(name = "trip_completed")),

            @AttributeOverride(name = "distance", column = @Column(name = "trip_distance")),
            @AttributeOverride(name = "trip_status", column = @Column(name = "trip_status")),

            // TimeFrame overrides
            @AttributeOverride(name = "timeFrame.startTime", column = @Column(name = "trip_start_time")),
            @AttributeOverride(name = "timeFrame.endTime", column = @Column(name = "trip_end_time")),

            // startLocation overrides
            @AttributeOverride(name = "startLocation.latitude", column = @Column(name = "trip_start_latitude")),
            @AttributeOverride(name = "startLocation.longitude", column = @Column(name = "trip_start_longitude")),

            // endLocation overrides
            @AttributeOverride(name = "endLocation.latitude", column = @Column(name = "trip_end_latitude")),
            @AttributeOverride(name = "endLocation.longitude", column = @Column(name = "trip_end_longitude"))
    })
    private Trip trip;

    public Booking(BookingId id, Long userId, Long vehicleId, TimeFrame timeFrame, GeoLocation pickupLocation) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.status = BookingStatus.CREATED;
        this.timeFrame = timeFrame;
        this.pickupLocation = pickupLocation;
        this.cost = 0.0;
    }

    public void confirm() {
        if (status != BookingStatus.CREATED) {
            throw new IllegalStateException("Booking cannot be confirmed from status: " + status);
        }
        status = BookingStatus.CONFIRMED;
    }

    public void startTrip(GeoLocation startLocation, LocalDateTime startTime) {
        if (status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Trip cannot be started from status: " + status);
        }

        trip = new Trip(UUID.randomUUID());
        trip.recordLocation(startLocation);

        timeFrame = new TimeFrame(startTime, timeFrame.getEndTime());
        status = BookingStatus.STARTED;

        pickupLocation = startLocation;
    }

    public void endTrip(GeoLocation endLocation, LocalDateTime endTime) {
        if (status != BookingStatus.STARTED || trip == null) {
            throw new IllegalStateException("Trip cannot be ended from status: " + status);
        }

        trip.recordLocation(endLocation);
        trip.complete();

        if (timeFrame.getEndTime() == null) {
            timeFrame.setEndTime(endTime);
        } else {
            // Create new TimeFrame if we can't modify the existing one
            timeFrame = new TimeFrame(timeFrame.getStartTime(), endTime);
        }
        trip.setTimeFrame(timeFrame);
        status = BookingStatus.COMPLETED;

        dropoffLocation = endLocation;
    }

    public void cancel() {
        if (status == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Completed booking cannot be cancelled");
        }

        if (status == BookingStatus.STARTED) {
            throw new IllegalStateException("Booking cannot be cancelled. Trip hast to be finished and payed first.");
        }

        status = BookingStatus.CANCELLED;
    }

    public void calculateCost(String billingModel, double rate) {
        if (status != BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot calculate cost: trip not completed");
        }

        if (trip == null) {
            throw new IllegalStateException("Cannot calculate cost: no trip information available");
        }

        double distanceInKm = trip.getDistanceInKm();
        long durationInMinutes = trip.getDurationInMinutes();

        double calculatedCost;
        if ("PER_HOUR".equals(billingModel)) {
            double hours = durationInMinutes / 60.0;
            calculatedCost = rate * hours;
        } else {
            calculatedCost = rate * distanceInKm;
        }

        // Round to 2 decimal places
        calculatedCost = Math.round(calculatedCost * 100.0) / 100.0;

        // Set the cost
        this.cost = calculatedCost;
    }

    public TripSummary createTripSummary(String billingModel, double rate) {
        if (status != BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot create summary: trip not completed");
        }

        TripSummary summary = new TripSummary();
        summary.setBookingId(id.getValue());
        summary.setCost(cost);
        summary.setDistanceInKm(trip.getDistanceInKm());
        summary.setDurationInMinutes(trip.getDurationInMinutes());
        summary.setBillingModel(billingModel);
        summary.setRate(rate);
        summary.setStartTime(timeFrame.getStartTime());
        summary.setEndTime(timeFrame.getEndTime());
        summary.setPickupLocation(pickupLocation);
        summary.setDropoffLocation(dropoffLocation);

        return summary;
    }

    public GeoLocation getFinalLocation() {
        if (status == BookingStatus.COMPLETED && dropoffLocation != null) {
            return dropoffLocation;
        } else if (trip != null && !trip.getRoute().isEmpty()) {
            // Return last recorded location in trip
            List<GeoLocation> route = trip.getRoute();
            return route.get(route.size() - 1);
        }
        return null;
    }
}

