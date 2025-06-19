package de.fleximove.vehicle.service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bookingService")
public interface BookingServiceClient {
    @DeleteMapping("${bookingService.deleteAllBookingsForVehicle.endpoint}")
    void deleteBookingsByVehicleId(@PathVariable Long vehicleId);
}

