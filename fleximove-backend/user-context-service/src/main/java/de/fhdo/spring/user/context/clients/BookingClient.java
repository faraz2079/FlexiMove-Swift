<<<<<<< HEAD:fleximove-backend/user-context-service/src/main/java/clients/BookingClient.java
package clients;
=======
package de.fhdo.spring.user.context.clients;
>>>>>>> origin/main:fleximove-backend/user-context-service/src/main/java/de/fhdo/spring/user/context/clients/BookingClient.java

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "bookingService")

<<<<<<< HEAD:fleximove-backend/user-context-service/src/main/java/clients/BookingClient.java
public interface BookingClient {
    @DeleteMapping("${bookingService.deleteUserBookings.endpoint}")
    	//@DeleteMapping("/api/bookings/user/{userId}")

=======
@FeignClient(name = "bookingService")
public interface BookingClient {
    @DeleteMapping("${bookingService.deleteAllBookings.endpoint}")
>>>>>>> origin/main:fleximove-backend/user-context-service/src/main/java/de/fhdo/spring/user/context/clients/BookingClient.java
    void deleteUserBookings(@PathVariable("userId") Long userId);
}

