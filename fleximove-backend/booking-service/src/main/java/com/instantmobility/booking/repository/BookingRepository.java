package com.instantmobility.booking.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.instantmobility.booking.domain.Booking;
import com.instantmobility.booking.domain.BookingId;

@Repository
public interface BookingRepository extends CrudRepository<Booking, BookingId> {
    List<Booking> findByUserId(UUID userId);
    List<Booking> findByVehicleId(UUID vehicleId);
    List<Booking> findByUserIdOrderByTimeFrame_StartTimeDesc(UUID userId);
	//List<Booking> findByUserIdOrderByTimeFrameDesc(UUID userId, int page, int size);
	void deleteByUserId(UUID userId);
}
