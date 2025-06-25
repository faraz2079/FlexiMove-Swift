import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { GeocodingService } from 'src/app/src/app/services/geocoding.service';
import { VehicleService } from 'src/app/src/app/services/vehicle.service';

export interface NearestAvailableVehicleResponse {
  vehicleId: number;
  vehicleModel: string;
  providerName: string;
  vehicleType: string;
  status: string;
  priceAmount: number;
  billingModel: string;
  address: string;
  latitude: number;
  longitude: number;
  distanceInKm: number;
  averageVehicleRating: number;
  averageProviderRating: number;
  restrictions: VehicleRestrictions
}

export interface VehicleRestrictions {
  maxBookingTimeMinutes: number;
  maxDistanceKm: number;
  maxPassengers: number;
  minAge: number;
  requiredLicense: string;
}


@Component({
  selector: 'app-customer-homepage',
  templateUrl: './customer-homepage.component.html',
  styleUrls: ['./customer-homepage.component.css']
})
export class CustomerHomepageComponent {
  selectedRadius: string = 'None';
  address: string = '';

  constructor(private router: Router, private geocodingService: GeocodingService, private snackBar: MatSnackBar, private vehicleService: VehicleService) {}

  ngOnInit(): void {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      alert('Bitte zuerst einloggen!');
      this.router.navigateByUrl('/login');
    }
  }
 
  searchVehicles() {
    if (!this.address) {
      this.snackBar.open('Please enter a valid address.', 'OK', { panelClass: 'snackbar-error' });
      return;
    }

    const parsedRadius = this.selectedRadius === 'None' ? undefined : parseFloat(this.selectedRadius);
    
    this.vehicleService.getNearbyVehicles(this.address, parsedRadius)
      .subscribe({
        next: (data) => {
          this.vehicleService.setResults(data);
          this.router.navigate(['/customer/search-results'], {
              queryParams: {
                address: this.address,
                radiusInKm: parsedRadius
              }
          });

        },
        error: (err) => {
          console.error(err);
          this.snackBar.open('Error fetching vehicles.', 'OK', { panelClass: 'snackbar-error' });
        }
      });
  }

  useCurrentLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        position => {
          const { latitude, longitude } = position.coords;
          this.geocodingService.reverseGeocode(latitude, longitude).subscribe({
            next: addr => this.address = addr,
            error: err => alert('Error while loading the address.')
          });
        },
        error => {
          alert('Unable to retrieve your location.');
          console.error(error);
        }
      );
    } else {
      alert('Geolocation is not supported by your current browser.');
    }
  }
}
