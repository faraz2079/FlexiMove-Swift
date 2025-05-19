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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getUserBookings(@PathVariable UUID userId) {
        List<BookingDTO> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }
}