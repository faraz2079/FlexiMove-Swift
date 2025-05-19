package com.instantmobility.booking.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class TimeFrame {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TimeFrame(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = Objects.requireNonNull(startTime, "Start time cannot be null");
        this.endTime = endTime;

        if (endTime != null && startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        if (endTime == null) {
            return null;
        }
        return Duration.between(startTime, endTime);
    }
}