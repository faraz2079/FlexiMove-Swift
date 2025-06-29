package com.instantmobility.booking.domain;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
@Embeddable
@NoArgsConstructor
public class TimeFrame {
    private  LocalDateTime startTime;
    private LocalDateTime endTime;

    public TimeFrame(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = Objects.requireNonNull(startTime, "Start time cannot be null");
        this.endTime = endTime;

        if (endTime != null && startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        if (endTime != null && startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}