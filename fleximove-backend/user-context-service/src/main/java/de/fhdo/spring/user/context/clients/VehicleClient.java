package de.fhdo.spring.user.context.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "vehicleService")
public interface VehicleClient {
    @DeleteMapping("${vehiclesService.deleteVehiclesByProvider.endpoint}")
    void deleteVehicle(@RequestParam("deleteForProviderId") Long deleteForProviderId);
}

