package clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import de.fhdo.spring.user.context.dto.BookingDto;

@FeignClient(name = "booking-service")
public interface BookingClient {
    @DeleteMapping("/bookings/user/{userId}")
    void deleteUserBookings(@PathVariable("userId") Long userId);
}