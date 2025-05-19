package com.instantmobility.booking.repository;

import com.instantmobility.booking.domain.Booking;
import com.instantmobility.booking.domain.BookingId;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BookingRepository {
    // In-memory repository for simplicity
    private final Map<UUID, Booking> bookings = new ConcurrentHashMap<>();

    public Optional<Booking> findById(BookingId id) {
        return Optional.ofNullable(bookings.get(id.getValue()));
    }

    public List<Booking> findByUserId(UUID userId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getUserId().equals(userId)) {
                result.add(booking);
            }
        }
        return result;
    }

    public List<Booking> findByVehicleId(UUID vehicleId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getVehicleId().equals(vehicleId)) {
                result.add(booking);
            }
        }
        return result;
    }

    public void save(Booking booking) {
        bookings.put(booking.getId().getValue(), booking);
    }
}