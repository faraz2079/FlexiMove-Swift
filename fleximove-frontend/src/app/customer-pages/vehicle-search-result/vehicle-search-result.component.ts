import { Component } from '@angular/core';
import { NearestAvailableVehicleResponse } from '../customer-homepage/customer-homepage.component';
import { VehicleService } from 'src/app/src/app/services/vehicle.service';

interface VehicleWithDetails extends NearestAvailableVehicleResponse {
  showDetails: boolean;
  restrictions?: VehicleRestrictions;
}

export interface VehicleRestrictions {
  maxBookingTimeMinutes: number;
  maxDistanceKm: number;
  maxPassengers: number;
  minAge: number;
  requiredLicense: string;
}

@Component({
  selector: 'app-vehicle-search-result',
  templateUrl: './vehicle-search-result.component.html',
  styleUrls: ['./vehicle-search-result.component.css']
})
export class VehicleSearchResultComponent {
  vehicles: VehicleWithDetails[] = [];

  constructor(private vehicleSearchService: VehicleService) {}

  ngOnInit(): void {
    this.vehicles = this.vehicleSearchService.getResults().map(vehicle => ({
      ...vehicle,
      showDetails: false
    }));
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



toggleDetails(vehicle: VehicleWithDetails): void {
  vehicle.showDetails = !vehicle.showDetails;

  if (vehicle.showDetails && vehicle.restrictions === undefined) {
    this.vehicleSearchService.getVehicleById(vehicle.vehicleId).subscribe({
      next: (response) => {
        console.log(response)
        vehicle.restrictions = response.restrictions; 
      },
      error: (err) => {
        alert('Could not load additional details.');
      }
    });
  }
}


}
