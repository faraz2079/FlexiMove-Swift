import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/src/app/services/user.service';
import { VehicleService } from 'src/app/src/app/services/vehicle.service';
import { Provider as FlexiProvider} from 'src/app/models/provider.model';

export interface ProviderVehicle {
  vehicleId: number;
  identNumber: string;
  vehicleModel: string;
  vehicleType: string;
  status: string;
  priceAmount: number;
  billingModel: string;
  address: string;
  latitude: number;
  longitude: number;
  averageVehicleRating: number;
  restrictions: VehicleRestrictions
}

export interface VehicleRestrictions {
  maxBookingTimeMinutes: number;
  maxDistanceKm: number;
  maxPassengers: number;
  minAge: number;
  requiredLicense: string;
}

interface ProviderVehicleWithDetails extends ProviderVehicle {
  showDetails: boolean;
}

@Component({
  selector: 'app-provider-homepage',
  templateUrl: './provider-homepage.component.html',
  styleUrls: ['./provider-homepage.component.css']
})
export class ProviderHomepageComponent {
  provider: FlexiProvider | null = null;
  foundVehicles: ProviderVehicleWithDetails[] = [];

  constructor(private router: Router, private vehicleService: VehicleService, private userService: UserService) {}
  

  ngOnInit(): void {
    const userId = localStorage.getItem('userId');
    
    if (!userId) {
      alert('Please log in first!');
      this.router.navigateByUrl('/login');
      return;
    }

    const currentUser = this.userService.getProvider();

    if (!currentUser) {
      this.userService.getUserById(userId).subscribe({
        next: (data) => {
          this.provider = data;
          this.loadProviderVehicles(+userId);
        },
        error: (err) => {
          console.error('Error while loading provider:', err);
          alert('Provider could not be loaded.');
        }
      });
    } else {
      this.provider = currentUser;
      this.loadProviderVehicles(+userId);
    }
  }

  loadProviderVehicles(providerId: number) {
    this.vehicleService.getVehiclesByProvider(providerId).subscribe({
      next: (vehicles) => {
        this.foundVehicles = vehicles.map(vehicle => ({
        ...vehicle,
        showDetails: false
      }));
      },
      error: (err) => {
        console.error('Could not load vehicles:', err);
        alert('Error loading provider vehicles.');
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

  toggleDetails(vehicle: ProviderVehicleWithDetails): void {
    vehicle.showDetails = !vehicle.showDetails;

    if (vehicle.showDetails && !vehicle.restrictions) {
      alert('No restriction data available for this vehicle.');
    }
  }
}
