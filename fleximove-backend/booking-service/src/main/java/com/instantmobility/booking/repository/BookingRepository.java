package com.instantmobility.booking.repository;

import com.instantmobility.booking.domain.Booking;
import com.instantmobility.booking.domain.BookingId;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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

    public void deleteByUserId(UUID userId) {
        // For in-memory implementation
        List<BookingId> toRemove = new ArrayList<>();

        for (Map.Entry<UUID, Booking> entry : bookings.entrySet()) {
            if (entry.getValue().getUserId().equals(userId)) {
                toRemove.add(new BookingId(entry.getKey()));
            }
        }

        for (BookingId id : toRemove) {
            bookings.remove(id.getValue());
        }
    }

    public List<Booking> findByUserIdOrderByTimeFrameDesc(UUID userId, int page, int size) {
        // For in-memory implementation
        return bookings.values().stream()
                .filter(booking -> booking.getUserId().equals(userId))
                .sorted((b1, b2) -> {
                    LocalDateTime time1 = b1.getTimeFrame().getStartTime();
                    LocalDateTime time2 = b2.getTimeFrame().getStartTime();
                    return time2.compareTo(time1); // Descending order
                })
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public void save(Booking booking) {
        bookings.put(booking.getId().getValue(), booking);
    }
}