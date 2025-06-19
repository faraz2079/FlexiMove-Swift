import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/src/app/services/user.service';
import { VehicleService } from 'src/app/src/app/services/vehicle.service';
import { Provider as FlexiProvider} from 'src/app/models/provider.model';
import { MatDialog } from '@angular/material/dialog';
import { RegisterVehicleDialogComponent } from '../register-vehicle-dialog/register-vehicle-dialog.component';
import { ConfirmDeleteDialogComponent } from '../confirm-delete-dialog/confirm-delete-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EditVehicleDialogComponent } from '../edit-vehicle-dialog/edit-vehicle-dialog.component';
import { RatingService } from 'src/app/src/app/services/rating.service';

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
  providerRating: number | null = null;

  constructor(private router: Router, private vehicleService: VehicleService, private userService: UserService, private dialog: MatDialog, private snackBar: MatSnackBar, private ratingService: RatingService) {}
  

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
          this.loadProviderRating(+userId);
        },
        error: (err) => {
          console.error('Error while loading provider:', err);
          alert('Provider could not be loaded.');
        }
      });
    } else {
      this.provider = currentUser;
      this.loadProviderVehicles(+userId);
      this.loadProviderRating(+userId);
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
        alert('Error loading your vehicles.');
      }
    });
  }

  loadProviderRating(providerId: number): void {
    this.ratingService.getProviderRating(providerId).subscribe({
      next: (rating) => {
        this.providerRating = rating;
      },
      error: (err) => {
        console.error('Could not load provider rating:', err);
        alert('Error loading your rating.');
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
      case 'BUS': return 'Bus';
      default: return '';
    }
  }

  formatVehicleStatus(status: string): string {
    switch (status) {
      case 'AVAILABLE': return 'Available';
      case 'BOOKED': return 'Booked';
      case 'IN_USE': return 'In use';
      case 'MAINTENANCE': return 'Maintenance';
      case 'RETIRED': return 'Retired';
      default: return '';
    }
  }

  toggleDetails(vehicle: ProviderVehicleWithDetails): void {
    vehicle.showDetails = !vehicle.showDetails;

    if (vehicle.showDetails && !vehicle.restrictions) {
      alert('No restriction data available for this vehicle.');
    }
  }

  openRegisterDialog(): void {
    const userId = localStorage.getItem('userId');

    if (!userId) {
      alert('User ID not found.');
      return;
    }

    const dialogRef = this.dialog.open(RegisterVehicleDialogComponent, {
      width: '600px',
      data: { providerId: userId }
    });

    dialogRef.afterClosed().subscribe((refresh) => {
      if (refresh) {
        this.loadProviderVehicles(+userId);
      }
    });
  }

  openDeleteDialog(vehicle: ProviderVehicle): void {
    const userId = localStorage.getItem('userId');

    if (!userId) {
      alert('User ID not found.');
      return;
    }

    const dialogRef = this.dialog.open(ConfirmDeleteDialogComponent, {
      width: '400px',
      data: { vehicleModel: vehicle.vehicleModel }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.vehicleService.deleteVehicle(vehicle.vehicleId).subscribe({
          next: () => {
            this.snackBar.open('Vehicle deleted successfully.', 'OK', { duration: 3000 });
            this.loadProviderVehicles(+userId);
          },
          error: (err) => {
            if (err.status === 404) {
              alert('Vehicle not found.');
            } else if (err.status === 409) {
              alert('Cannot delete vehicle while it is booked or in use or when the booking is either active or not paid.');
            } else if (err.status === 502) {
              alert('Payment information deletion failed. Please try again later.');
            } else {
              alert('Unexpected error while deleting vehicle.');
            }
          }
        });
      }
    });
  }

  openEditDialog(vehicle: ProviderVehicle): void {
    const userId = localStorage.getItem('userId');

    if (!userId) {
      alert('User ID not found.');
      return;
    }

    const dialogRef = this.dialog.open(EditVehicleDialogComponent, {
      width: '600px',
      data: { vehicle }
    });

    dialogRef.afterClosed().subscribe(refresh => {
      if (refresh) {
        this.loadProviderVehicles(+userId);
      }
    });
  }


}
