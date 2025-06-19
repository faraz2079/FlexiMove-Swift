package de.fhdo.spring.user.context.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ratingService")
public interface RatingClient {
    @DeleteMapping("${ratingService.deleteRatingsByCustomerId.endpoint}")
    void deleteCustomerRatingsByUserId(@PathVariable Long customerId);

    @DeleteMapping("${ratingService.deleteAllProviderRatings.endpoint}")
    void deleteAllProviderRatings(@PathVariable Long providerId);
}
