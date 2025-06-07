package clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="BOOKINGSERVICE")
public interface BookingClient {
	//@DeleteMapping("/api/bookings/user/{userId}")
    @DeleteMapping("${bookingService.deleteUserBookings.endpoint}")

    void deleteUserBookings(@PathVariable Long userId);
}