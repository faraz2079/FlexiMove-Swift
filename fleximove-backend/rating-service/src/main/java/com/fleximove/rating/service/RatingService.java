package com.fleximove.rating.service;

import com.fleximove.rating.model.RatingProvider;
import com.fleximove.rating.model.RatingVehicle;
import com.fleximove.rating.repository.RatingProviderRepository;
import com.fleximove.rating.repository.RatingVehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class RatingService {

    @Autowired
    private RatingVehicleRepository ratingVehicleRepository;

    @Autowired
    private RatingProviderRepository ratingProviderRepository;

    public RatingVehicle rateVehicle(RatingVehicle rating) {
        return ratingVehicleRepository.save(rating);
    }

    public RatingProvider rateProvider(RatingProvider rating) {
        return ratingProviderRepository.save(rating);
    }

    public double getAverageRatingForVehicle(Long vehicleId) {
        Double avgVehicleRating;
        try {
            avgVehicleRating = ratingVehicleRepository.findAverageScoreByVehicleId(vehicleId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No ratings found for this vehicle."));
        } catch (Exception e) {
            avgVehicleRating = 0.0;
        }
        return avgVehicleRating;
    }

    public double getAverageRatingForProvider(Long providerId) {
        Double avgProviderRating;
        try {
            avgProviderRating = ratingProviderRepository.findAverageScoreByProviderId(providerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No ratings found for this provider."));
        } catch (Exception e) {
            avgProviderRating = 0.0;
        }
        return avgProviderRating;
    }
}