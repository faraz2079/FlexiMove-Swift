package com.instantmobility.booking.repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.instantmobility.booking.domain.Booking;
import com.instantmobility.booking.domain.BookingId;

@Repository
public interface BookingRepository extends JpaRepository<Booking, BookingId> {
    List<Booking> findByUserId(UUID userId);
    List<Booking> findByVehicleId(UUID vehicleId);
    List<Booking> findByUserIdOrderByTimeFrame_StartTimeDesc(UUID userId);
	//List<Booking> findByUserIdOrderByTimeFrameDesc(UUID userId, int page, int size);
	void deleteByUserId(UUID userId);
    @Query("SELECT b FROM Booking b WHERE b.userId = :userId ORDER BY b.timeFrame.startTime DESC")
    List<Booking> findByUserIdOrderByTimeFrameDesc(
            @Param("userId") UUID userId,
            Pageable pageable
    );
}
