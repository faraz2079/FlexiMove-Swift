package com.instantmobility.booking.clients;

import com.instantmobility.booking.domain.GeoLocation;
import com.instantmobility.booking.domain.VehicleStatus;
import com.instantmobility.booking.dto.BillingInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "vehicleService")
public interface VehicleServiceClient {
    @PatchMapping("${vehicleService.updateVehicleStatus.endpoint}")
    ResponseEntity<Void> updateVehicleStatus(@PathVariable Long vehicleId, @RequestParam VehicleStatus newStatus);

    @PatchMapping("${vehicleService.vehicleUpdateLocation.endpoint}")
    ResponseEntity<Void> updateVehicleLocation(@PathVariable Long vehicleId, @RequestBody GeoLocation locationData);

    @GetMapping("/api/vehicles/{vehicleId}/billing")
    BillingInfo getBillingInfo(@PathVariable Long vehicleId);
}



