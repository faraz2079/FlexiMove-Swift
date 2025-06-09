import { Component } from '@angular/core';
import { NearestAvailableVehicleResponse } from '../customer-homepage/customer-homepage.component';
import { VehicleService } from 'src/app/src/app/services/vehicle.service';
import { ActivatedRoute, Router } from '@angular/router';

interface VehicleWithDetails extends NearestAvailableVehicleResponse {
  showDetails: boolean;
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


  constructor(private vehicleSearchService: VehicleService, private route: ActivatedRoute, private router: Router) {}

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

}
