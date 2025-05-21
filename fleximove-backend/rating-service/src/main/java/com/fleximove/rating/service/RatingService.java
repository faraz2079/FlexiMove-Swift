package com.fleximove.rating.service;

import com.fleximove.rating.model.Rating;
import com.fleximove.rating.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating addRating(Rating rating) {
        rating.setTimestamp(LocalDateTime.now());
        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsByVehicle(UUID vehicleId) {
        return ratingRepository.findByVehicleId(vehicleId);
    }

    public List<Rating> getRatingsByProvider(UUID providerId) {
        return ratingRepository.findByProviderId(providerId);
    }

    public double getAverageRatingForVehicle(UUID vehicleId) {
        Double avg = ratingRepository.findAverageScoreByVehicleId(vehicleId).orElse(null);
        if (avg == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No ratings found for this vehicle.");
        }
        return avg;
    }


    public double getAverageRatingForProvider(UUID providerId) {
        Double avg = ratingRepository.findAverageScoreByProviderId(providerId).orElse(null);
        if (avg == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No ratings found for this provider.");
        }
        return avg;
    }

}
