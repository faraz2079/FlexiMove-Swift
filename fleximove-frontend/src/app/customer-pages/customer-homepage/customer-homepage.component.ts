import { Component } from '@angular/core';

@Component({
  selector: 'app-customer-homepage',
  templateUrl: './customer-homepage.component.html',
  styleUrls: ['./customer-homepage.component.css']
})
export class CustomerHomepageComponent {
  selectedRadius = '';
  address: string = '';
 
  searchVehicles() {
  console.log(`Searching vehicles at ${this.address} within ${this.selectedRadius} km radius.`);
}
}
