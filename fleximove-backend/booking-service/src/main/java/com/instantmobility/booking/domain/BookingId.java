package com.instantmobility.booking.domain;

import java.util.Objects;
import java.util.UUID;

public class BookingId {
    private final UUID value;

    public BookingId(UUID value) {
        this.value = Objects.requireNonNull(value, "BookingId value cannot be null");
    }

    public static BookingId generate() {
        return new BookingId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingId bookingId = (BookingId) o;
        return Objects.equals(value, bookingId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}