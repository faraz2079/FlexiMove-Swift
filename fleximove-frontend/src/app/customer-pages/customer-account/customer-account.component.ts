import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Customer } from 'src/app/models/customer.model';
import { UserService } from 'src/app/src/app/services/user.service';
import { DeleteAccountDialogComponent } from '../delete-account-dialog/delete-account-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-customer-account',
  templateUrl: './customer-account.component.html',
  styleUrls: ['./customer-account.component.css']
})
export class CustomerAccountComponent {
  user: Customer = {
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    phoneNumber: '',
    email: { value: '' },
    password: '',
    driverLicenseType: 'NONE',
    address: { street: '', city: '', postcode: '', country: '' },
    paymentinfo: { cardHolderName: '', creditCardNumber: '', cvv: '', expiryDate: '' }
  };

  constructor(private router: Router, private userService: UserService, private dialog: MatDialog) {}
  
    ngOnInit(): void {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        alert('Please log in first!');
        this.router.navigateByUrl('/login');
        return;
      }

      const currentUser = this.userService.getCustomer();

      if (!currentUser) {
        this.userService.getCustomerById(userId).subscribe({
        next: (data) => {
          this.user = data;
        },
        error: (err) => {
          console.error('Error while loading user:', err);
          alert('User could not be loaded.');
        }
      });
      }
      else {
        this.user = currentUser;
      }
    }

    openDeleteDialog(): void {
      const dialogRef = this.dialog.open(DeleteAccountDialogComponent);

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          alert("Account deleted!")
          localStorage.clear();
          this.router.navigateByUrl('/login');
        }
      });
    }
}
