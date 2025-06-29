import { Component } from '@angular/core';
import { NearestAvailableVehicleResponse } from '../customer-homepage/customer-homepage.component';
import { VehicleService } from 'src/app/src/app/services/vehicle.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from 'src/app/src/app/services/user.service';
import { BookingService } from 'src/app/src/app/services/booking.service';

interface VehicleWithDetails extends NearestAvailableVehicleResponse {
  showDetails: boolean;
}

export interface CreateBookingRequest {
  userId: number;
  vehicleId: number;
  bookedAt: string;
  pickupLatitude: number;
  pickupLongitude: number;
  userAge: number;
  userLicenseType: string;
  vehicleMinimumAge: number;
  vehicleRequiredLicense: string;
}

@Component({
  selector: 'app-vehicle-search-result',
  templateUrl: './vehicle-search-result.component.html',
  styleUrls: ['./vehicle-search-result.component.css']
})
export class VehicleSearchResultComponent {
  vehicles: VehicleWithDetails[] = [];
  filteredVehicles: VehicleWithDetails[] = [];
  selectedRadius: number | undefined;
  address: string = '';

  filtersAndSortingOption = {
    selectedSortOption: '',
    vehicleType: '',
    maxBookingTimeMinutes: null,
    maxDistanceKm: null,
    maxPassengers: null,
    minAge: null,
    requiredLicense: ''
  };


  constructor(private vehicleSearchService: VehicleService, private route: ActivatedRoute, private router: Router, private bookingService: BookingService, private snackBar: MatSnackBar, private userService: UserService) {}

  ngOnInit(): void {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      alert('Please log in first!');
      this.router.navigateByUrl('/login');
      return;
    }

    const existingResults = this.vehicleSearchService.getResults();

    if (existingResults.length > 0) {
      this.vehicles = existingResults.map(vehicle => ({
        ...vehicle,
        showDetails: false
      }));
      this.filteredVehicles = [...this.vehicles];
    } else {
      this.route.queryParams.subscribe(params => {
        this.address = params['address'];
        this.selectedRadius = +params['radiusInKm'] || undefined;

        if (this.address) {
          this.vehicleSearchService.getNearbyVehicles(this.address, this.selectedRadius).subscribe(data => {
            this.vehicles = data.map(vehicle => ({
              ...vehicle,
              showDetails: false
            }));
            this.filteredVehicles = [...this.vehicles];
          });
        }
      });
    }
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

  toggleDetails(vehicle: VehicleWithDetails): void {
    vehicle.showDetails = !vehicle.showDetails;

    if (vehicle.showDetails && !vehicle.restrictions) {
      alert('No restriction data available for this vehicle.');
    }
  }


  applyFilters(): void {
    this.filteredVehicles = [...this.vehicles].filter(vehicle => {
      const r = vehicle.restrictions;

      if (!r){
        return false;
      }

      const matchesType =
        !this.filtersAndSortingOption.vehicleType || vehicle.vehicleType === this.filtersAndSortingOption.vehicleType;
      
      const matchesPassengers =
        !this.filtersAndSortingOption.maxPassengers || r?.maxPassengers >= this.filtersAndSortingOption.maxPassengers;

      const matchesAge =
        !this.filtersAndSortingOption.minAge || r?.minAge <= this.filtersAndSortingOption.minAge;

      const matchesLicense =
        !this.filtersAndSortingOption.requiredLicense || r?.requiredLicense === this.filtersAndSortingOption.requiredLicense;

      const matchesBookingTime =
        !this.filtersAndSortingOption.maxBookingTimeMinutes || r?.maxBookingTimeMinutes >= this.filtersAndSortingOption.maxBookingTimeMinutes;

      const matchesMaxDistance =
        !this.filtersAndSortingOption.maxDistanceKm || r?.maxDistanceKm >= this.filtersAndSortingOption.maxDistanceKm;


      return matchesType && matchesPassengers && matchesAge && matchesLicense && matchesBookingTime && matchesMaxDistance;
    });
  }

  applySorting(): void {
    const sort = this.filtersAndSortingOption.selectedSortOption;

    switch (sort) {
      case 'priceAsc':
        this.filteredVehicles.sort((a, b) => a.priceAmount - b.priceAmount);
        break;
      case 'priceDesc':
        this.filteredVehicles.sort((a, b) => b.priceAmount - a.priceAmount);
        break;
      case 'distanceAsc':
        this.filteredVehicles.sort((a, b) => a.distanceInKm - b.distanceInKm);
        break;
      case 'distanceDesc':
        this.filteredVehicles.sort((a, b) => b.distanceInKm - a.distanceInKm);
        break;
      case 'providerRatingDesc':
        this.filteredVehicles.sort((a, b) => (b.averageProviderRating ?? 0) - (a.averageProviderRating ?? 0));
        break;
      case 'vehicleRatingDesc':
        this.filteredVehicles.sort((a, b) => (b.averageVehicleRating ?? 0) - (a.averageVehicleRating ?? 0));
        break;
    }
  }


  resetFiltersAndSorting(): void {
    this.filtersAndSortingOption = {
      selectedSortOption: '',
      vehicleType: '',
      maxBookingTimeMinutes: null,
      maxDistanceKm: null,
      maxPassengers: null,
      minAge: null,
      requiredLicense: ''
    };
    this.filteredVehicles = [...this.vehicles];
  }

  mapUserLicenseToVehicleLicense(code: string): string {
    switch (code) {
      case 'A': return 'MOTORCYCLE';
      case 'B': return 'CAR';
      case 'C': return 'TRUCK';
      case 'D': return 'BUS';
      case 'NONE': return 'NONE';
      default: return code;
    }
  }

  bookVehicle(vehicle: NearestAvailableVehicleResponse): void {
    const userId = localStorage.getItem('userId');

    this.userService.getUserById(userId!).subscribe(user => {
      const request: CreateBookingRequest = {
        userId: +userId!,
        vehicleId: vehicle.vehicleId,
        bookedAt: this.getBerlinLocalISOString(),
        pickupLatitude: vehicle.latitude,
        pickupLongitude: vehicle.longitude,
        userAge: this.calculateAge(user.dateOfBirth),
        userLicenseType: this.mapUserLicenseToVehicleLicense(user.driverLicenseType),
        vehicleMinimumAge: vehicle.restrictions.minAge,
        vehicleRequiredLicense: vehicle.restrictions.requiredLicense
      };

      console.log("Request sent to backend")
      console.log(request)

      this.bookingService.createBooking(request).subscribe({
        next: (response: any) => {
          this.snackBar.open('Vehicle is booked successfully! You can find it under "My Bookings".', 'Close', { duration: 4000 });
          this.filteredVehicles = this.filteredVehicles.filter(v => v.vehicleId !== vehicle.vehicleId);
          this.vehicles = this.vehicles.filter(v => v.vehicleId !== vehicle.vehicleId);
        },
        error: (err) => {
          if (err.status === 422) {
            this.snackBar.open('Validation failed: ' + err.error?.error, 'Close', { duration: 5000 });
        } else if (err.status === 500) {
            this.snackBar.open('Server error: ' + err.error?.error, 'Close', { duration: 5000 });
        } else {
            this.snackBar.open('Booking failed. Please try again.', 'Close', { duration: 5000 });
        }
        }
      });
    });
  }


 calculateAge(dateOfBirth: string): number {
    const birthDate = new Date(dateOfBirth);
    const today = new Date();

    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (
      monthDiff < 0 ||
      (monthDiff === 0 && today.getDate() < birthDate.getDate())
    ) {
      age--;
    }

    return age;
  }

  getBerlinLocalISOString(): string {
    const berlinTime = new Date().toLocaleString("sv-SE", { timeZone: "Europe/Berlin" });
    return berlinTime.replace(" ", "T");
  }
}
