package com.instantmobility.booking.clients;

import com.instantmobility.booking.domain.GeoLocation;
import com.instantmobility.booking.domain.VehicleStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "vehicleService")
public interface VehicleServiceClient {
    @PatchMapping("${vehicleService.updateVehicleStatus.endpoint}")
    ResponseEntity<Void> updateVehicleStatus(@PathVariable Long vehicleId, @RequestParam VehicleStatus newStatus);

    @PatchMapping("${vehicleService.vehicleUpdateLocation.endpoint}")
    ResponseEntity<Void> updateVehicleLocation(@PathVariable Long vehicleId, @RequestBody GeoLocation locationData);
}
