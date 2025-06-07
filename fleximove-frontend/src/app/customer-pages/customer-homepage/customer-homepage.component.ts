import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { GeocodingService } from 'src/app/src/app/services/geocoding.service';

@Component({
  selector: 'app-customer-homepage',
  templateUrl: './customer-homepage.component.html',
  styleUrls: ['./customer-homepage.component.css']
})
export class CustomerHomepageComponent {
  selectedRadius = 'None';
  address: string = '';

  constructor(private router: Router, private geocodingService: GeocodingService /*private userService: UserService */) {}

  ngOnInit(): void {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      alert('Bitte zuerst einloggen!');
      this.router.navigateByUrl('/login');
    }
  }
 
  searchVehicles() {
  console.log(`Searching vehicles at ${this.address} within ${this.selectedRadius} km radius.`);
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
