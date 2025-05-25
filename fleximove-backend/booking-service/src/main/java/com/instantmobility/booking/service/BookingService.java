package com.instantmobility.booking.service;

import com.instantmobility.booking.domain.*;
import com.instantmobility.booking.dto.BookingDTO;
import com.instantmobility.booking.dto.CreateBookingRequest;
import com.instantmobility.booking.dto.EndTripRequest;
import com.instantmobility.booking.dto.StartTripRequest;
import com.instantmobility.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public UUID createBooking(CreateBookingRequest request) {
        // Create booking domain object
        BookingId bookingId = BookingId.generate();
        TimeFrame timeFrame = new TimeFrame(request.getStartTime());
        GeoLocation pickupLocation = new GeoLocation(request.getPickupLatitude(), request.getPickupLongitude());

        Booking booking = new Booking(bookingId, request.getUserId(), request.getVehicleId(), timeFrame, pickupLocation);
        booking.confirm();

        // Save booking
        bookingRepository.save(booking);

        return bookingId.getValue();
    }

    public void startTrip(UUID bookingId, StartTripRequest request) {
        Booking booking = getBookingById(bookingId);

        GeoLocation startLocation = new GeoLocation(request.getStartLatitude(), request.getStartLongitude());
        LocalDateTime startTime = request.getStartTime() != null ? request.getStartTime() : LocalDateTime.now();

        booking.startTrip(startLocation, startTime);

        bookingRepository.save(booking);
    }

    public void endTrip(UUID bookingId, EndTripRequest request) {
        Booking booking = getBookingById(bookingId);

        GeoLocation endLocation = new GeoLocation(request.getEndLatitude(), request.getEndLongitude());
        LocalDateTime endTime = request.getEndTime() != null ? request.getEndTime() : LocalDateTime.now();

        booking.endTrip(endLocation, endTime);

        // Update vehicle location when trip ends
        updateVehicleLocation(booking.getVehicleId(), endLocation);

        bookingRepository.save(booking);
    }

    public void cancelBooking(UUID bookingId) {
        Booking booking = getBookingById(bookingId);

        booking.cancel();

        bookingRepository.save(booking);
    }

    /**
     * Updates vehicle location when booking ends
     */
    private void updateVehicleLocation(UUID vehicleId, GeoLocation location) {
        // In a real application, this would call the Vehicle service
        // For now, we'll just log it
        System.out.println("Vehicle " + vehicleId + " location updated to: " +
                location.getLatitude() + ", " + location.getLongitude());

        // If you had a VehicleService client:
        // vehicleServiceClient.updateLocation(vehicleId, location);
    }

    /**
     * Deletes all bookings for a specific user
     */
    public void deleteBookingsByUserId(UUID userId) {
        List<Booking> userBookings = bookingRepository.findByUserId(userId);

        for (Booking booking : userBookings) {
            // Don't allow deletion of active bookings
            if (booking.getStatus() == BookingStatus.STARTED ||
                    booking.getStatus() == BookingStatus.CONFIRMED) {
                throw new IllegalStateException(
                        "Cannot delete user with active bookings. Cancel bookings first.");
            }
        }

        // Delete all bookings for this user
        bookingRepository.deleteByUserId(userId);
    }

    /**
     * Gets booking history for a user with pagination
     */
    public List<BookingDTO> getBookingHistory(UUID userId, int page, int size) {
        List<Booking> bookings = bookingRepository.findByUserIdOrderByTimeFrameDesc(userId, page, size);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO getBookingDetails(UUID bookingId) {
        Booking booking = getBookingById(bookingId);
        return mapToDTO(booking);
    }

    public List<BookingDTO> getUserBookings(UUID userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private Booking getBookingById(UUID bookingId) {
        return bookingRepository.findById(new BookingId(bookingId))
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));
    }

    private BookingDTO mapToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId().getValue());
        dto.setUserId(booking.getUserId());
        dto.setVehicleId(booking.getVehicleId());
        dto.setStatus(booking.getStatus().name());
        dto.setStartTime(booking.getTimeFrame().getStartTime());
        dto.setEndTime(booking.getTimeFrame().getEndTime());
        dto.setCost(booking.getCost());

        if (booking.getPickupLocation() != null) {
            dto.setPickupLatitude(booking.getPickupLocation().getLatitude());
            dto.setPickupLongitude(booking.getPickupLocation().getLongitude());
        }

        if (booking.getDropoffLocation() != null) {
            dto.setDropoffLatitude(booking.getDropoffLocation().getLatitude());
            dto.setDropoffLongitude(booking.getDropoffLocation().getLongitude());
        }

        if (booking.getTrip() != null) {
            dto.setDistance(booking.getTrip().getDistance());
        }

        return dto;
    }
}
