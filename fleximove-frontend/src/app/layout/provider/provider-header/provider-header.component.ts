import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-provider-header',
  templateUrl: './provider-header.component.html',
  styleUrls: ['./provider-header.component.css']
})
export class ProviderHeaderComponent {
  dropdownOpen = false;

  constructor(private router: Router) {}

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  logout(): void {
    localStorage.clear();
    this.router.navigateByUrl('/login');
  }
}
