package com.fleximove.rating.controller;

import com.fleximove.rating.model.Rating;
import com.fleximove.rating.service.RatingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ratings")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public Rating addRating(@RequestBody Rating rating) {
        return ratingService.addRating(rating);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<Rating> getRatingsByVehicle(@PathVariable UUID vehicleId) {
        return ratingService.getRatingsByVehicle(vehicleId);
    }

    @GetMapping("/provider/{providerId}")
    public List<Rating> getRatingsByProvider(@PathVariable UUID providerId) {
        return ratingService.getRatingsByProvider(providerId);
    }

    @GetMapping("/vehicle/{vehicleId}/average")
    public double getAverageRatingForVehicle(@PathVariable UUID vehicleId) {
        return ratingService.getAverageRatingForVehicle(vehicleId);
    }

    @GetMapping("/provider/{providerId}/average")
    public double getAverageRatingForProvider(@PathVariable UUID providerId) {
        return ratingService.getAverageRatingForProvider(providerId);
    }
}