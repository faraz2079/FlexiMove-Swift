package com.fleximove.rating.repository;

import com.fleximove.rating.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
    List<Rating> findByVehicleId(UUID vehicleId);
    List<Rating> findByProviderId(UUID providerId);

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.vehicleId = :vehicleId")
    Optional<Double> findAverageScoreByVehicleId(@Param("vehicleId") UUID vehicleId);

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.providerId = :providerId")
    Optional<Double> findAverageScoreByProviderId(@Param("providerId") UUID providerId);
}
