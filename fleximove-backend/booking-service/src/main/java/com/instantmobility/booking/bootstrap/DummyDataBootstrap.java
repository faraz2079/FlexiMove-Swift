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

    private void initData() {
    	long count = bookingRepository.count();
    	System.out.println("initData called — booking in DB: " + count);
    	if (count > 0) return;
        Booking booking1 = new Booking(
            BookingId.generate(),
            TEST_USER_ID,
            UUID.randomUUID(),
            new TimeFrame(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)),
            new GeoLocation(52.5200, 13.4050)
        );
        booking1.confirm();
        booking1.startTrip(new GeoLocation(52.5200, 13.4050), LocalDateTime.now().minusDays(2));
        booking1.endTrip(new GeoLocation(52.5150, 13.4100), LocalDateTime.now().minusDays(1));

        Booking booking2 = new Booking(
            BookingId.generate(),
            TEST_USER_ID,
            UUID.randomUUID(),
            new TimeFrame(LocalDateTime.now().plusDays(1)),
            new GeoLocation(48.1351, 11.5820)
        );

        Booking booking3 = new Booking(
            BookingId.generate(),
            TEST_USER_ID,
            UUID.randomUUID(),
            new TimeFrame(LocalDateTime.now().minusHours(5), LocalDateTime.now().minusHours(3)),
            new GeoLocation(50.1109, 8.6821)
        );
        booking3.confirm();
        booking3.cancel();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        // Jetzt die Buchungen abfragen und ausgeben:
        List<Booking> bookings = bookingRepository.findByUserIdOrderByTimeFrame_StartTimeDesc(TEST_USER_ID);
        System.out.println("Bookings for TEST_USER_ID:");
        bookings.forEach(b -> System.out.println("Booking ID: " + b.getId() + ", Status: " + b.getStatus() + ", StartTime: " + b.getTimeFrame().getStartTime()));
    }
}