package de.fleximove.vehicle.service.controller;

import de.fleximove.vehicle.service.domain.valueobject.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.fleximove.vehicle.service.services.VehicleService;
import de.fleximove.vehicle.service.dto.VehicleRequest;

//TODO: think about dividing Controller into internal API (e.g. ProviderVehicleController, UserVehicleController)
//TODO: exception handling
@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/registeredBy/{providerId}")
    public ResponseEntity<Void> registerVehicle(@RequestBody VehicleRequest request, @PathVariable Long providerId) {
        vehicleService.registerNewVehicle(request, providerId);
        return ResponseEntity.ok().build();
    }

    /*@GetMapping("/load/{id}")
    public ResponseEntity<VehicleResponse> getVehicle(@PathVariable Long id) {
        return vehicleService.getVehicleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity.BodyBuilder deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok();
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<VehicleResponse>> getNearbyVehicles(@RequestParam Long userId, @RequestParam double radiusInKm) {
        Location userLocation = userServiceClient.getLocationByUserId(userId);
        //TODO: überlegen, ob hier vllt auch eine Anfrage an Rating erfolgt, um Ratings nachzuladen
        List<VehicleResponse> vehicles = vehicleService.findAvailableNearbyVehicles(userLocation, radiusInKm);
        return ResponseEntity.ok(vehicles);
    }

    //TODO: edit vehicle information
    @PutMapping("/edit/{vehicleId}/by/{providerId}")
    public ResponseEntity<Void> editVehicle(@PathVariable Long vehicleId, @RequestBody EditVehicleRequest request, @PathVariable Long providerId) {
        vehicleService.editVehicleInformation(vehicleId, request, providerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/providerVehiclesList/{providerId}")
    public ResponseEntity<List<VehicleResponse>> listProviderVehicles(@PathVariable Long providerId) {
        var vehicles = vehicleService.listVehiclesByProvider(providerId);
        return ResponseEntity.ok(vehicles);
    }*/


}
