package com.fleximove.rating.repository;

import com.fleximove.rating.model.RatingVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingVehicleRepository extends JpaRepository<RatingVehicle, Long> {

    @Query("SELECT AVG(r.score) FROM RatingVehicle r WHERE r.vehicleId = :vehicleId")
    Optional<Double> findAverageScoreByVehicleId(Long vehicleId);
}