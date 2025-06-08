package clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "bookingService")

public interface BookingClient {
    @DeleteMapping("${bookingService.deleteUserBookings.endpoint}")
    	//@DeleteMapping("/api/bookings/user/{userId}")

    void deleteUserBookings(@PathVariable("userId") Long userId);
}

