import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customer-homepage',
  templateUrl: './customer-homepage.component.html',
  styleUrls: ['./customer-homepage.component.css']
})
export class CustomerHomepageComponent {
  selectedRadius = '';
  address: string = '';

  constructor(private router: Router, /*private userService: UserService */) {}

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
}
