package com.instantmobility.booking.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

public class Booking {
    private final BookingId id;
    private final UUID userId;
    private final UUID vehicleId;
    private BookingStatus status;
    private TimeFrame timeFrame;
    private GeoLocation pickupLocation;
    private GeoLocation dropoffLocation;
    private double cost;
    private Trip trip;

    public Booking(BookingId id, UUID userId, UUID vehicleId, TimeFrame timeFrame, GeoLocation pickupLocation) {
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
            ((TimeFrame)timeFrame).setEndTime(endTime); // Cast to access setter
        } else {
            // Create new TimeFrame if we can't modify the existing one
            timeFrame = new TimeFrame(timeFrame.getStartTime(), endTime);
        }
        status = BookingStatus.COMPLETED;

        dropoffLocation = endLocation;

        // Calculate cost based on distance and duration
        calculateCost();
    }

    public void cancel() {
        if (status == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Completed booking cannot be cancelled");
        }

        status = BookingStatus.CANCELLED;
    }

    private void calculateCost() {
        // Simple cost calculation based on distance and time
        double baseFare = 2.50;
        double perKmRate = 1.25;
        double perMinuteRate = 0.35;

        double distanceCost = 0;
        double timeCost = 0;

        if (trip != null) {
            distanceCost = trip.getDistance() * perKmRate;
        }

        if (timeFrame.getEndTime() != null) {
            long minutes = timeFrame.getDuration().toMinutes();
            timeCost = minutes * perMinuteRate;
        }

        cost = baseFare + distanceCost + timeCost;
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
    // Getters
    public BookingId getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public TimeFrame getTimeFrame() {
        return timeFrame;
    }

    public GeoLocation getPickupLocation() {
        return pickupLocation;
    }

    public GeoLocation getDropoffLocation() {
        return dropoffLocation;
    }

    public double getCost() {
        return cost;
    }

    public Trip getTrip() {
        return trip;
    }
}
