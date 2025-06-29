package com.instantmobility.booking.service;

import com.instantmobility.booking.clients.PaymentServiceClient;
import com.instantmobility.booking.clients.VehicleServiceClient;
import com.instantmobility.booking.domain.*;
import com.instantmobility.booking.dto.*;
import com.instantmobility.booking.repository.BookingRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.ErrorManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final PaymentServiceClient paymentServiceClient;

    private final VehicleServiceClient vehicleServiceClient;

    @Autowired
    public BookingService(BookingRepository bookingRepository, PaymentServiceClient paymentServiceClient, VehicleServiceClient vehicleServiceClient) {
        this.bookingRepository = bookingRepository;
        this.paymentServiceClient = paymentServiceClient;
        this.vehicleServiceClient = vehicleServiceClient;
    }


    @Transactional
    public UUID createBooking(CreateBookingRequest request) {
        validateUserForVehicle(request);
        BookingId bookingId = BookingId.generate();
        GeoLocation pickupLocation = new GeoLocation(request.getPickupLatitude(), request.getPickupLongitude());

        Booking booking = new Booking(bookingId, request.getUserId(), request.getVehicleId(), request.getBookedAt(), pickupLocation);

        booking.confirm();

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            vehicleServiceClient.updateVehicleStatus(booking.getVehicleId(), VehicleStatus.BOOKED);
        }
        bookingRepository.save(booking);

        return bookingId.getValue();
    }

    private void validateUserForVehicle(CreateBookingRequest request) {
        // Age validation
        if (request.getUserAge() < request.getVehicleMinimumAge()) {
            throw new ValidationException(
                    "User age (" + request.getUserAge() + ") is below the minimum required age ("
                            + request.getVehicleMinimumAge() + ") for this vehicle"
            );
        }

        // License validation
        if (request.getVehicleRequiredLicense() != null && !request.getVehicleRequiredLicense().isEmpty()) {
            if (request.getUserLicenseType() == null || request.getUserLicenseType().isEmpty()) {
                throw new ValidationException("User license is required for this vehicle");
            }

            // Case-insensitive string comparison
            if (!request.getUserLicenseType().equalsIgnoreCase(request.getVehicleRequiredLicense())) {
                throw new ValidationException(
                        "User license type (" + request.getUserLicenseType()
                                + ") does not match the required license for this vehicle: "
                                + request.getVehicleRequiredLicense()
                );
            }
        }
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

    @Transactional
    public TripSummary endTrip(UUID bookingId, EndTripRequest request) {
        Booking booking = getBookingById(bookingId);

        // End the trip
        GeoLocation endLocation = new GeoLocation(request.getEndLatitude(), request.getEndLongitude());
        LocalDateTime endTime = request.getEndTime() != null ? request.getEndTime() : LocalDateTime.now();
        booking.endTrip(endLocation, endTime);

        BillingInfo billingInfo = vehicleServiceClient.getBillingInfo(booking.getVehicleId());

        booking.calculateCost(billingInfo.getBillingModel(), billingInfo.getRate());

        TripSummary summary = booking.createTripSummary(
                billingInfo.getBillingModel(),
                billingInfo.getRate()
        );

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            vehicleServiceClient.updateVehicleStatus(booking.getVehicleId(), VehicleStatus.AVAILABLE);
            vehicleServiceClient.updateVehicleLocation(booking.getVehicleId(), booking.getFinalLocation());
        }

        bookingRepository.save(booking);

        return summary;
    }

    @Transactional
    @CircuitBreaker(name = "paymentService", fallbackMethod = "processPaymentFallback")
    public PaymentResponse processPayment(UUID bookingId, PaymentRequest request) {
        Booking booking = getBookingById(bookingId);

        if (!booking.getUserId().equals(request.getUserId())) {
            throw new ValidationException("Booking does not belong to this user");
        }

        // Validate booking is in the right state
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Booking must be in COMPLETED state to process payment, current state: " + booking.getStatus()
            );
        }

        PaymentRequest paymentRequest = new PaymentRequest(request.getUserId(), bookingId, request.getAmount(), request.getDescription());

        PaymentResponse response = paymentServiceClient.processPayment(paymentRequest);

        if ("COMPLETED".equals(response.getPaymentStatus())) {
            booking.setStatus(BookingStatus.PAID);
            booking.setPaymentId(response.getPaymentId());
            bookingRepository.save(booking);
        }

        return response;
    }


    @Transactional
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
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(booking.getUserId());
        paymentRequest.setBookingId(booking.getId().getValue());
        paymentRequest.setAmount(booking.getCost());
        paymentRequest.setDescription("Ride payment for booking " + booking.getId().getValue());

        try {
            PaymentResponse response = paymentServiceClient.processPayment(paymentRequest);
            System.out.println("Payment processed: " + response.getPaymentId());
        } catch (Exception e) {
            System.err.println("Payment failed: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteBookingsByUserId(Long userId) {
        List<Booking> userBookings = bookingRepository.findByUserId(userId);

        if (!userBookings.isEmpty()) {
           for (Booking booking : userBookings) {
                if (booking.getStatus() != BookingStatus.PAID &&
                        booking.getStatus() != BookingStatus.CANCELLED) {
                    throw new IllegalStateException(
                            "Cannot delete user with active or not paid bookings. Either bookings have to be paid or trips have to be cancelled first.");
                }
            }

            try {
                boolean hasPaidBookings = userBookings.stream()
                        .anyMatch(b -> b.getStatus() == BookingStatus.PAID);

                if (hasPaidBookings) {
                    try {
                        paymentServiceClient.deletePaymentsByUser(userId);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to delete payment info for user " + userId, e);
                    }
                }
                bookingRepository.deleteByUserId(userId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete bookings for user " + userId, e);
            }
        }
    }

    @Transactional
    public void deleteBookingsAndPaymentsByVehicle(Long vehicleId) {
        List<Booking> bookings = bookingRepository.findByVehicleId(vehicleId);

        if (!bookings.isEmpty()) {
            for (Booking booking : bookings) {
                if (booking.getStatus() != BookingStatus.PAID &&
                        booking.getStatus() != BookingStatus.CANCELLED) {
                    throw new IllegalStateException(
                            "Cannot delete vehicle with active or not paid booking.");
                }
            }

            try {
                List<Booking> deletableBookings = bookings.stream()
                        .filter(b -> b.getStatus() == BookingStatus.PAID)
                        .toList();

                if (!deletableBookings.isEmpty()) {
                    try {
                        for (Booking b : deletableBookings) {
                            paymentServiceClient.deletePaymentsByBookingId(b.getId().getValue());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to delete payment info for vehicle " + vehicleId, e);
                    }
                }
                bookingRepository.deleteByVehicleId(vehicleId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete bookings for vehicle " + vehicleId, e);
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
        List<Booking> bookings = bookingRepository.findByUserIdOrderByBookedAtDesc(userId);
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
        Trip trip = booking.getTrip();
        dto.setId(booking.getId().getValue());
        dto.setUserId(booking.getUserId());
        dto.setVehicleId(booking.getVehicleId());
        dto.setStatus(booking.getStatus().name());
        dto.setBookedAt(booking.getBookedAt());
        dto.setCost(booking.getCost());

        if (trip != null) {
            if (trip.getTimeFrame() != null) {
                dto.setStartTime(trip.getTimeFrame().getStartTime());
                dto.setEndTime(trip.getTimeFrame().getEndTime());
                dto.setDurationInMinutes(trip.getDurationInMinutes());
            }

            dto.setDistance(booking.getTrip().getDistance());
        }

        if (booking.getPickupLocation() != null) {
            dto.setPickupLatitude(booking.getPickupLocation().getLatitude());
            dto.setPickupLongitude(booking.getPickupLocation().getLongitude());
        }

        if (booking.getDropoffLocation() != null) {
            dto.setDropoffLatitude(booking.getDropoffLocation().getLatitude());
            dto.setDropoffLongitude(booking.getDropoffLocation().getLongitude());
        }

        return dto;
    }
}
