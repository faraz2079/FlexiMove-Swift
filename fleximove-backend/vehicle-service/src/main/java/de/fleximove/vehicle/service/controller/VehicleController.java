package de.fleximove.vehicle.service.controller;

import de.fleximove.vehicle.service.domain.Vehicle;
import de.fleximove.vehicle.service.domain.valueobject.Location;
import de.fleximove.vehicle.service.dto.NearestAvailableVehicleResponse;
import de.fleximove.vehicle.service.services.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.fleximove.vehicle.service.services.VehicleService;
import de.fleximove.vehicle.service.dto.VehicleRequest;

import java.util.List;

//TODO: think about dividing Controller into internal API (e.g. ProviderVehicleController, UserVehicleController)
//TODO: exception handling
@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final GeocodingService geocodingService;

    @Autowired
    public VehicleController(VehicleService vehicleService, GeocodingService geocodingService) {
        this.vehicleService = vehicleService;
        this.geocodingService = geocodingService;
    }

    @PostMapping("/registeredBy")
    public ResponseEntity<Void> registerVehicle(@RequestBody VehicleRequest request, @RequestParam Long providerId) {
        vehicleService.registerNewVehicle(request, providerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<?> getVehicle(@PathVariable Long id) {
        try {
            return vehicleService.fetchVehicleById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fehler beim Abrufen des Fahrzeugs: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<NearestAvailableVehicleResponse>> getNearbyVehicles(
            @RequestParam String address,
            @RequestParam(required = false, defaultValue = "3.0") double radiusInKm) {
        Location neededLocation = geocodingService.geocodeAddress(address);
        List<NearestAvailableVehicleResponse> vehicles = vehicleService.findAvailableNearbyVehicles(neededLocation, radiusInKm);
        return ResponseEntity.ok(vehicles);
    }


    /*
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

    //TODO: look for vehicles with other status

}
