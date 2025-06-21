package com.fleximove.rating.controller;

import com.fleximove.rating.model.RatingProvider;
import com.fleximove.rating.model.RatingVehicle;
import com.fleximove.rating.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    RatingController(RatingService ratingService){
        this.ratingService = ratingService;
    }

    @PostMapping("/vehicle")
    public ResponseEntity<RatingVehicle> rateVehicle(@RequestBody RatingVehicle rating) {
        return ResponseEntity.ok(ratingService.rateVehicle(rating));
    }

    @PostMapping("/provider")
    public ResponseEntity<RatingProvider> rateProvider(@RequestBody RatingProvider rating) {
        return ResponseEntity.ok(ratingService.rateProvider(rating));
    }

    @GetMapping("/vehicle/{vehicleId}/average")
    public ResponseEntity<Double> getAverageVehicleRating(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(ratingService.getAverageRatingForVehicle(vehicleId));
    }

    @GetMapping("/provider/{providerId}/average")
    public ResponseEntity<Double> getAverageProviderRating(@PathVariable Long providerId) {
        return ResponseEntity.ok(ratingService.getAverageRatingForProvider(providerId));
    }

    @DeleteMapping("/customerRatings/{customerId}/deleteAll")
    public ResponseEntity<Void> deleteCustomerRatingsByUserId(@PathVariable Long customerId) {
        ratingService.deleteAllCustomerRatings(customerId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAll/forRatedProvider/{providerId}")
    public ResponseEntity<Void> deleteProviderRatingsByProviderId(@PathVariable Long providerId) {
        ratingService.deleteAllProviderRatings(providerId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAll/forRatedVehicle/{vehicleId}")
    public ResponseEntity<Void> deleteVehicleRatingsByVehicleId(@PathVariable Long vehicleId) {
        ratingService.deleteAllVehicleRatings(vehicleId);
        return ResponseEntity.noContent().build();
    }
}