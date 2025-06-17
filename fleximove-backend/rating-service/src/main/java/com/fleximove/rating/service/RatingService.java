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

    private final RatingVehicleRepository vehicleRepository;

    private final RatingProviderRepository providerRepository;

    @Autowired
    RatingService(RatingProviderRepository providerRepository, RatingVehicleRepository vehicleRepository){
        this.providerRepository = providerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public RatingVehicle rateVehicle(RatingVehicle rating) {
        return vehicleRepository.save(rating);
    }

    public RatingProvider rateProvider(RatingProvider rating) {
        return providerRepository.save(rating);
    }

    public double getAverageRatingForVehicle(Long vehicleId) {
        return vehicleRepository.findAverageScoreByVehicleId(vehicleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No ratings found for this vehicle."));
    }

    public double getAverageRatingForProvider(Long providerId) {
        return providerRepository.findAverageScoreByProviderId(providerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No ratings found for this provider."));
    }
}