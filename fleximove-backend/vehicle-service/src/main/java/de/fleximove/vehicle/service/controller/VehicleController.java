package de.fleximove.vehicle.service.controller;

import de.fleximove.vehicle.service.domain.valueobject.BillingModel;
import de.fleximove.vehicle.service.domain.valueobject.Location;
import de.fleximove.vehicle.service.domain.valueobject.Price;
import de.fleximove.vehicle.service.domain.valueobject.VehicleStatus;
import de.fleximove.vehicle.service.dto.*;
import de.fleximove.vehicle.service.services.GeocodingService;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.fleximove.vehicle.service.services.VehicleService;

import java.util.List;

//TODO for future: split controller
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

    //Request kommt aus Frontend
    @PostMapping("/registeredBy")
    public ResponseEntity<?> registerVehicle(@RequestBody VehicleRequest request, @RequestParam Long providerId) {
        try {
            vehicleService.registerNewVehicle(request, providerId);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A vehicle with this identification number already exists.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error during registration.");
        }
    }

    //Request kommt aus Frontend oder aus RatingService oder aus BookingService
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

    //Request kommt aus Frontend, wird von Provider getriggered
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (FeignException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    //Request kommt aus UserService
    @DeleteMapping("/deleteAllVehicles")
    public ResponseEntity<?> deleteVehiclesByProvider(@RequestParam Long deleteForProviderId) {
        try {
            vehicleService.deleteAllVehiclesByProviderId(deleteForProviderId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred while deleting vehicles.");
        }
    }

    //Request kommt aus Frontend
    @GetMapping("/nearby")
    public ResponseEntity<List<NearestAvailableVehicleResponse>> getNearbyVehicles(
            @RequestParam String address,
            @RequestParam(required = false, defaultValue = "3.0") double radiusInKm) {
        Location neededLocation = geocodingService.geocodeAddress(address);
        List<NearestAvailableVehicleResponse> vehicles = vehicleService.findAvailableNearbyVehicles(neededLocation, radiusInKm);
        return ResponseEntity.ok(vehicles);
    }

    //Request kommt aus Frontend, wird beim Laden von Provider-Profile getriggered
    @GetMapping("/providerVehiclesList")
    public ResponseEntity<List<ProviderVehicleResponse>> listProviderVehicles(@RequestParam Long forProviderId) {
        List<ProviderVehicleResponse> vehicles = vehicleService.listProviderVehiclesWithRatings(forProviderId);
        return ResponseEntity.ok(vehicles);
    }

    //Request kommt aus Frontend, wird von Provider getriggered
    @PatchMapping("/edit/{vehicleId}")
    public ResponseEntity<?> editVehicle(@PathVariable Long vehicleId, @RequestBody EditVehicleRequest request) {
        try {
            vehicleService.editVehicleInformation(vehicleId, request);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A vehicle with this identification number already exists.");
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found: " + e.getMessage());
        }
        catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    //Request kommt aus dem BookingService, wird waehrend Booking getriggered
    @PatchMapping("/updateStatus/{vehicleId}")
    public ResponseEntity<Void> updateVehicleStatus(@PathVariable Long vehicleId, @RequestParam VehicleStatus newStatus
    ) {
        vehicleService.updateVehicleStatus(vehicleId, newStatus);
        return ResponseEntity.ok().build();
    }

    //Request kommt aus dem BookingService
    @PatchMapping("/updateLocation/{vehicleId}")
    public ResponseEntity<Void> updateVehicleLocation(@PathVariable Long vehicleId, @RequestBody Location locationData) {
        vehicleService.updateVehicleLocation(
                vehicleId,
                locationData.getLatitude(),
                locationData.getLongitude()
        );
        return ResponseEntity.ok().build();
    }

    //Request kommt aus dem BookingService
    @GetMapping("/{vehicleId}/billing")
    public ResponseEntity<?> getBillingInfo(@PathVariable Long vehicleId) {
        try {
            BillingInfo billingModel = vehicleService.getBillingModel(vehicleId);
            return ResponseEntity.ok(billingModel);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
        }
    }


}
