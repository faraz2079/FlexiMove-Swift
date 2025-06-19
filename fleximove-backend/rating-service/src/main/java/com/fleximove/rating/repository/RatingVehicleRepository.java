package com.fleximove.rating.repository;

import com.fleximove.rating.model.RatingVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingVehicleRepository extends JpaRepository<RatingVehicle, UUID> {
    @Query("SELECT AVG(r.score) FROM RatingVehicle r WHERE r.vehicleId = :vehicleId")
    Optional<Double> findAverageScoreByVehicleId(Long vehicleId);
    void deleteByUserId(Long userId);
    void deleteByVehicleId(Long vehicleId);
    List<RatingVehicle> findByVehicleId(Long vehicleId);
    List<RatingVehicle> findByUserId(Long userId);
}