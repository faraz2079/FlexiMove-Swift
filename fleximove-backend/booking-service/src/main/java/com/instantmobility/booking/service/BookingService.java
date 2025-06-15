package com.instantmobility.booking.service;

import com.instantmobility.booking.clients.PaymentServiceClient;
import com.instantmobility.booking.clients.VehicleServiceClient;
import com.instantmobility.booking.domain.*;
import com.instantmobility.booking.dto.*;
import com.instantmobility.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final PaymentServiceClient paymentService;

    private final VehicleServiceClient vehicleServiceClient;

    @Autowired
    public BookingService(BookingRepository bookingRepository, PaymentServiceClient paymentService, VehicleServiceClient vehicleServiceClient) {
        this.bookingRepository = bookingRepository;
        this.paymentService = paymentService;
        this.vehicleServiceClient = vehicleServiceClient;
    }


    public UUID createBooking(CreateBookingRequest request) {
        // Create booking domain object
        BookingId bookingId = BookingId.generate();
        TimeFrame timeFrame = new TimeFrame(request.getStartTime());
        GeoLocation pickupLocation = new GeoLocation(request.getPickupLatitude(), request.getPickupLongitude());

        Booking booking = new Booking(bookingId, request.getUserId(), request.getVehicleId(), timeFrame, pickupLocation);
        //TODO in real application: check whether user fulfills the vehicle restrictions; request to the vehicleServiceClient
        //TODO for now: only check whether the user has neccessary license and his age
        booking.confirm();

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            vehicleServiceClient.updateVehicleStatus(booking.getVehicleId(), VehicleStatus.BOOKED);
        }
        bookingRepository.save(booking);

        return bookingId.getValue();
    }

    public void startTrip(UUID bookingId, StartTripRequest request) {
        Booking booking = getBookingById(bookingId);

        GeoLocation startLocation = new GeoLocation(request.getStartLatitude(), request.getStartLongitude());
        LocalDateTime startTime = request.getStartTime() != null ? request.getStartTime() : LocalDateTime.now();

        booking.startTrip(startLocation, startTime);
        if (booking.getStatus() == BookingStatus.STARTED) {
            vehicleServiceClient.updateVehicleStatus(booking.getVehicleId(), VehicleStatus.IN_USE);
        }
        bookingRepository.save(booking);
    }

    public void endTrip(UUID bookingId, EndTripRequest request) {
        Booking booking = getBookingById(bookingId);

        GeoLocation endLocation = new GeoLocation(request.getEndLatitude(), request.getEndLongitude());
        LocalDateTime endTime = request.getEndTime() != null ? request.getEndTime() : LocalDateTime.now();

        booking.endTrip(endLocation, endTime);

        try {
            //TODO: create separate Payment API in controller where it will trigger payment Service then. Payment service should also receive provider's billing model, price, duration and distance
            //TODO: create updateBookingStatus method in order to change booking status to PAID
            processPaymentForBooking(booking);
        } catch (Exception e) {
            throw new RuntimeException("Payment failed for booking " + bookingId + ": " + e.getMessage(), e);
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            try {
                updateVehicleLocationAndStatus(booking.getVehicleId(), endLocation);
            } catch (Exception e) {
                throw new RuntimeException("Failed to update vehicle location or status for booking " + bookingId + ": " + e.getMessage(), e);
            }
        }

        bookingRepository.save(booking);
    }

    public void cancelBooking(UUID bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.cancel();
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            vehicleServiceClient.updateVehicleStatus(booking.getVehicleId(), VehicleStatus.AVAILABLE);
        }
        bookingRepository.save(booking);
    }

    /**
     * Updates vehicle location when booking ends
     */
    private void updateVehicleLocationAndStatus(Long vehicleId, GeoLocation location) {
        vehicleServiceClient.updateVehicleLocation(vehicleId, location);
        vehicleServiceClient.updateVehicleStatus(vehicleId, VehicleStatus.AVAILABLE);
        System.out.println("Vehicle " + vehicleId + " location updated to: " +
                location.getLatitude() + ", " + location.getLongitude());
    }

    private void processPaymentForBooking(Booking booking) {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setUserId(booking.getUserId());
        paymentRequest.setBookingId(booking.getId().getValue());
        paymentRequest.setAmount(BigDecimal.valueOf(booking.getCost()));
        paymentRequest.setDescription("Ride payment for booking " + booking.getId().getValue());

        try {
            PaymentResponseDTO response = paymentService.processPayment(paymentRequest);
            // Store payment reference in booking or handle accordingly
            System.out.println("Payment processed: " + response.getPaymentId());
        } catch (Exception e) {
            // Handle payment failure
            System.err.println("Payment failed: " + e.getMessage());
        }
    }

    /**
     * Deletes all bookings for a specific user
     */
    public void deleteBookingsByUserId(Long userId) {
        List<Booking> userBookings = bookingRepository.findByUserId(userId);

        if (!userBookings.isEmpty()) {
           for (Booking booking : userBookings)
            {
                // Don't allow deletion of active bookings
                if (booking.getStatus() != BookingStatus.PAID &&
                        booking.getStatus() != BookingStatus.CANCELLED) {
                    throw new IllegalStateException(
                            "Cannot delete user with active bookings. Either bookings have to be canceled or trips have to be finished first.");
                }
            }

            try {
                bookingRepository.deleteBookingsByUserId(userId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete bookings for user " + userId, e);
            }
        }
    }

    /**
     * Gets booking history for a user with pagination
     
    public List<BookingDTO> getBookingHistory(Long userId, int page, int size) {
        List<Booking> bookings = bookingRepository.findByUserIdOrderByTimeFrameDesc(userId, page, size);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
*/
    public BookingDTO getBookingDetails(UUID bookingId) {
        Booking booking = getBookingById(bookingId);
        return mapToDTO(booking);
    }

    public List<BookingDTO> getUserBookings(Long userId) {
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
