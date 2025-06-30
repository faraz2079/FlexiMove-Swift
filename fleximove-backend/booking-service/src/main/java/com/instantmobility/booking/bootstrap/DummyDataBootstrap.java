package com.instantmobility.booking.bootstrap;

import com.instantmobility.booking.domain.*;
import com.instantmobility.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class DummyDataBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final BookingRepository bookingRepository;

    @Autowired
    public DummyDataBootstrap(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_VEHICLE_ID = 3L;

    private void initData() {
    	long count = bookingRepository.count();
    	System.out.println("initData called — booking in DB: " + count);
    	if (count > 0) return;

        Booking booking1 = new Booking(
            BookingId.generate(),
            TEST_USER_ID, TEST_VEHICLE_ID, LocalDateTime.now().minusDays(1),
            new GeoLocation(51.958282249999996, 6.9994736934575705)
        );
        booking1.confirm();
        bookingRepository.save(booking1);
    }
}