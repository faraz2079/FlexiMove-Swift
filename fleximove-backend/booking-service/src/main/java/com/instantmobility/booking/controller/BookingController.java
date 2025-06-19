package com.instantmobility.booking.controller;

import com.instantmobility.booking.dto.BookingDTO;
import com.instantmobility.booking.dto.CreateBookingRequest;
import com.instantmobility.booking.dto.EndTripRequest;
import com.instantmobility.booking.dto.StartTripRequest;
import com.instantmobility.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<UUID> createBooking(@RequestBody CreateBookingRequest request) {
        UUID bookingId = bookingService.createBooking(request);
        return new ResponseEntity<>(bookingId, HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}/start")
    public ResponseEntity<Void> startTrip(@PathVariable UUID bookingId, @RequestBody StartTripRequest request) {
        bookingService.startTrip(bookingId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bookingId}/end")
    //TODO: dont send Void back, but some DTO with distance, duration and totalCost to show in the frontend
    public ResponseEntity<Void> endTrip(@PathVariable UUID bookingId, @RequestBody EndTripRequest request) {
        bookingService.endTrip(bookingId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable UUID bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingDetails(@PathVariable UUID bookingId) {
        BookingDTO booking = bookingService.getBookingDetails(bookingId);
        return ResponseEntity.ok(booking);
    }

    /**
     * Delete all bookings for a user
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUserBookings(@PathVariable Long userId) {
        try {
            bookingService.deleteBookingsByUserId(userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Unexpected error while deleting bookings for this vehicle.");
        }
    }

    @DeleteMapping("/deleteAll/byVehicle/{vehicleId}")
    public ResponseEntity<?> deleteBookingsByVehicle(@PathVariable Long vehicleId) {
        try {
            bookingService.deleteBookingsAndPaymentsByVehicle(vehicleId);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Unexpected error while deleting bookings for this vehicle.");
        }
    }


    /**
     * Get booking history with pagination
    
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<BookingDTO>> getBookingHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<BookingDTO> history = bookingService.getBookingHistory(userId, page, size);
        return ResponseEntity.ok(history);
    } */

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getUserBookings(@PathVariable Long userId) {
        List<BookingDTO> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }
}