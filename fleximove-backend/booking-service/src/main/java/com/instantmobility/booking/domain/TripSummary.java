


package com.instantmobility.booking.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TripSummary {
    private UUID bookingId;
    private BigDecimal cost;
    private double distanceInKm;
    private long durationInMinutes;
    private String billingModel;
    private BigDecimal rate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private GeoLocation pickupLocation;
    private GeoLocation dropoffLocation;

    public UUID getBookingId() {
        return bookingId;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public double getDistanceInKm() {
        return distanceInKm;
    }

    public long getDurationInMinutes() {
        return durationInMinutes;
    }

    public String getBillingModel() {
        return billingModel;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public GeoLocation getPickupLocation() {
        return pickupLocation;
    }

    public GeoLocation getDropoffLocation() {
        return dropoffLocation;
    }

    // Setters
    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public void setDistanceInKm(double distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public void setDurationInMinutes(long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public void setBillingModel(String billingModel) {
        this.billingModel = billingModel;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setPickupLocation(GeoLocation pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public void setDropoffLocation(GeoLocation dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }
}
