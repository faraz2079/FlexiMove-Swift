package com.instantmobility.booking.clients;

import com.instantmobility.booking.domain.GeoLocation;
import com.instantmobility.booking.domain.VehicleStatus;
import com.instantmobility.booking.dto.BillingInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "vehicleService")
public interface VehicleServiceClient {
    @PatchMapping("${vehicleService.updateVehicleStatus.endpoint}")
    ResponseEntity<Void> updateVehicleStatus(@PathVariable Long vehicleId, @RequestParam VehicleStatus newStatus);

    @PatchMapping("${vehicleService.vehicleUpdateLocation.endpoint}")
    ResponseEntity<Void> updateVehicleLocation(@PathVariable Long vehicleId, @RequestBody GeoLocation locationData);

    @GetMapping("${vehicleService.billingInfo.endpoint}")
    BillingInfo getBillingInfo(@PathVariable Long vehicleId);
}



