import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BookingService } from 'src/app/src/app/services/booking.service';
import { GeocodingService } from 'src/app/src/app/services/geocoding.service';
import { RatingService } from 'src/app/src/app/services/rating.service';
import { UserService } from 'src/app/src/app/services/user.service';
import { VehicleService } from 'src/app/src/app/services/vehicle.service';
import { VehicleRestrictions } from '../customer-homepage/customer-homepage.component';
import { forkJoin, map, Observable, of, switchMap } from 'rxjs';

export interface BookingDTO {
  id: string;
  userId: number;
  vehicleId: number;
  status: string;
  startTime: string;
  endTime: string;
  pickupLatitude: number;
  pickupLongitude: number;
  dropoffLatitude: number;
  dropoffLongitude: number;
  cost: number;
  distance: number;
}

interface BookingDetails extends BookingDTO{
  vehicleModel: string;
  identificationNumber: string,
  providerName: string;
  providerId: number,
  vehicleType: string;
  priceAmount: number;
  billingModel: string;
  pickupAddress: string,
  dropoffAddress: string,
  averageVehicleRating: number;
  averageProviderRating: number;
  restrictions: VehicleRestrictions,
  showDetails: boolean;
  tripStartedAt?: string;
  tripEndedAt?: string;
  duration?: number;
}

export interface RatingVehicleDTO {
  vehicleId: number;
  userId: number;
  score: number;
  comment?: string;
}

export interface RatingProviderDTO {
  providerId: number;
  userId: number;
  score: number;
  comment?: string;
}

export interface StartTripRequest {
  startLatitude: number;
  startLongitude: number;
  startTime: string;
}

export interface EndTripRequest {
  endLatitude: number;
  endLongitude: number;
  endTime: string;
}

export interface GeoLocation {
  latitude: number;
  longitude: number;
}

export interface TripSummary {
  bookingId: string;
  cost: number;
  distanceInKm: number;
  durationInMinutes: number;
  billingModel: string;
  rate: number;
  startTime: string;
  endTime: string;
  pickupLocation: GeoLocation;
  dropoffLocation: GeoLocation;
}


@Component({
  selector: 'app-my-bookings',
  templateUrl: './my-bookings.component.html',
  styleUrls: ['./my-bookings.component.css']
})
export class MyBookingsComponent implements OnInit {
  bookings: BookingDetails[] = [];

  constructor(private bookingService: BookingService, private router: Router, private geocodingService: GeocodingService, private vehicleService: VehicleService, private userService: UserService, private ratingService: RatingService) {}

  ngOnInit(): void {
    this.loadBookings();
  }

  private loadBookings(): void {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      alert('Please log in first!');
      this.router.navigateByUrl('/login');
      return;
    }

    this.bookingService.getUserBookings(+userId).pipe(
      switchMap((bookings: BookingDTO[]) => {
        const enriched$ = bookings.map((booking: BookingDTO) =>
          forkJoin({
            vehicle: this.vehicleService.getVehicleById(booking.vehicleId),
            pickupAddress: this.geocodingService.reverseGeocode(booking.pickupLatitude, booking.pickupLongitude),
            dropoffAddress: booking.dropoffLatitude
              ? this.geocodingService.reverseGeocode(booking.dropoffLatitude, booking.dropoffLongitude)
              : of('Trip has not been finished yet'),
          }).pipe(
            switchMap(({ vehicle, pickupAddress, dropoffAddress }) =>
              forkJoin({
                providerName: this.userService.getCompanyName(vehicle.providerId),
                vehicleRating: this.ratingService.getVehicleRating(vehicle.id),
                providerRating: this.ratingService.getProviderRating(vehicle.providerId)
              }).pipe(
                map(({ providerName, vehicleRating, providerRating }) => ({
                  ...booking,
                  vehicleModel: vehicle.vehicleModel,
                  identificationNumber: vehicle.identificationNumber.identNumber,
                  vehicleType: vehicle.vehicleType,
                  billingModel: vehicle.vehiclePrice.billingModel,
                  priceAmount: vehicle.vehiclePrice.amount,
                  providerName,
                  providerId: vehicle.providerId,
                  pickupAddress,
                  dropoffAddress,
                  averageVehicleRating: vehicleRating,
                  averageProviderRating: providerRating,
                  restrictions: vehicle.restrictions
                }) as BookingDetails)
              )
            )
          )
        );
        return forkJoin(enriched$);
      })
    ).subscribe({
      next: enrichedBookings => {
        this.bookings = enrichedBookings;
        console.log(this.bookings)
      },
      error: err => {
        console.log(err.message)
        alert('Fehler beim Laden der Buchungen');
        this.bookings = [];
      }
    });

  }

  toggleDetails(booking: BookingDetails): void {
    booking.showDetails = !booking.showDetails;

    if (booking.showDetails && !booking.restrictions) {
      alert('No restriction data available for this vehicle.');
    }
  }

  onVehicleRatingChange(score: number, booking: BookingDetails): void {
    const userId = Number(localStorage.getItem('userId'));
    const rating: RatingVehicleDTO = {
      vehicleId: booking.vehicleId,
      userId,
      score
    };

    this.ratingService.submitVehicleRating(rating).subscribe({
      next: () => alert('Vehicle rated successfully!'),
      error: () => alert('Error during vehicle rating')
    });
  }

  onProviderRatingChange(score: number, booking: BookingDetails): void {
    const userId = Number(localStorage.getItem('userId'));
    const rating: RatingProviderDTO = {
      providerId: booking.providerId,
      userId,
      score
    };

    this.ratingService.submitProviderRating(rating).subscribe({
      next: () => alert('Provider rated successfully!'),
      error: () => alert('Error during provider rating')
    });
  }


  startTrip(booking: BookingDetails): void {
    if (!navigator.geolocation) {
      alert('Geolocation is not supported by your browser');
      return;
    }

    //In real application we would use the Vehicle GPS
    navigator.geolocation.getCurrentPosition(
      position => {
        booking.tripStartedAt = new Date().toISOString()
        const request: StartTripRequest = {
          startLatitude: position.coords.latitude,
          startLongitude: position.coords.longitude,
          startTime: booking.tripStartedAt
        };

        this.bookingService.startTrip(booking.id, request).subscribe({
          next: () => {
            booking.status = 'STARTED';
            alert('Trip started successfully.');
          },
          error: err => {
            alert('Failed to start the trip.');
            console.error(err);
          }
        });
      },
      error => {
        alert('Could not get your location.');
        console.error(error);
      }
    );
  }


  endTrip(booking: BookingDetails): void {
    if (!navigator.geolocation) {
      alert('Geolocation not supported');
      return;
    }

    //In real application we would use the Vehicle GPS
    navigator.geolocation.getCurrentPosition(
      position => {
        booking.tripEndedAt = new Date().toISOString()
        const request: EndTripRequest = {
          endLatitude: position.coords.latitude,
          endLongitude: position.coords.longitude,
          endTime: booking.tripEndedAt
        };

        this.bookingService.endTrip(booking.id, request).subscribe({
          next: summary => {
            booking.status = 'COMPLETED';
            booking.cost = summary.cost;
            booking.distance = summary.distanceInKm;
            booking.duration = summary.durationInMinutes;

            alert(`Trip finished. Duration: ${summary.durationInMinutes} minutes, total amount: ${summary.cost}€`);
          },
          error: err => {
            alert('Failed to finish the trip.');
            console.error(err);
          }
        });
      },
      error => {
        alert('Could not get your location.');
      }
    );
  }


  cancelBooking(booking: BookingDetails): void {
    this.bookingService.cancelBooking(booking.id).subscribe({
      next: () => {
        booking.status = 'CANCELLED';
        alert('Booking was cancelled.');
      },
      error: err => {
        alert('Failed to cancel booking.');
        console.error(err);
      }
    });
  }


  pay(booking: any): void {
    const userId = Number(localStorage.getItem('userId'));
    const paymentRequest = {
      userId: userId,
      bookingId: booking.id,
      amount: booking.cost,
      description: `Payment for booking ${booking.id}`
    };

    console.log("Payment request: {}", paymentRequest)

    this.bookingService.processPayment(booking.id, paymentRequest).subscribe({
      next: (response) => {
        booking.status = 'PAID';
        alert('Payment successful!');
      },
      error: (error) => {
        console.error('Payment failed:', error);
        alert('Payment failed: ' + (error.error?.message || 'Unknown error'));
      }
    });
  }


formatBillingModel(billingModel: string): string {
    switch (billingModel) {
      case 'PER_KILOMETER': return 'kilometer';
      case 'PER_HOUR': return 'hour';
      default: return billingModel.toLowerCase();
    }
  }

  formatLicense(code: string): string {
    switch (code) {
      case 'MOTORCYCLE': return 'A';
      case 'CAR': return 'B';
      case 'TRUCK': return 'C';
      case 'BUS': return 'D';
      case 'NONE': return 'No license required';
      default: return code;
    }
  }

  formatMinAge(age: number | null | undefined): string {
    if (!age || age === 0) {
      return 'No minimum age';
    }
    return `${age} years`;
  }

  formatVehicleType(type: string): string {
    switch (type) {
      case 'CAR': return 'Car';
      case 'TRUCK': return 'Truck';
      case 'BICYCLE': return 'Bicycle';
      case 'E_BIKE': return 'E-Bike';
      case 'E_SCOOTER': return 'E-Scooter';
      case 'SCOOTER': return 'Scooter';
      case 'MOTORCYCLE': return 'Motorcycle';
      default: return '';
    }
  }


}
