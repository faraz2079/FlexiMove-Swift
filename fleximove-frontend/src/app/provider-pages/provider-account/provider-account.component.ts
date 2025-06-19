import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Customer } from 'src/app/models/customer.model';
import { UserService } from 'src/app/src/app/services/user.service';
import { MatDialog } from '@angular/material/dialog';
import { Provider } from 'src/app/models/provider.model';
import { DeleteAccountDialogComponent } from 'src/app/delete-account-dialog/delete-account-dialog.component';

@Component({
  selector: 'app-provider-account',
  templateUrl: './provider-account.component.html',
  styleUrls: ['./provider-account.component.css']
})
export class ProviderAccountComponent {
  user: Provider = {
    companyName: '',
    phoneNumber: '',
    email: { value: '' },
    password: {value: ''},
    address: { street: '', city: '', postcode: '', country: '' },
    paymentinfo: { cardHolderName: '', creditCardNumber: '', cvv: '', expiryDate: '' },
  };

  passwordData = {
    oldPassword: '',
    newPassword: ''
  };

  constructor(private router: Router, private userService: UserService, private dialog: MatDialog) {}
  
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
          const userId = localStorage.getItem('userId');

          if (!userId) {
            alert('User ID not found.');
            return;
          }

          this.userService.deleteAccount(+userId).subscribe({
              next: () => {
                alert('Account deleted!');
                localStorage.clear();
                this.router.navigateByUrl('/login');
              },
              error: err => {
                  if (err.status === 404) {
                    alert('Error: Account or related vehicles not found.');
                  }
                  else if (err.status === 409) {
                      alert('Account deletion failed. Some vehicles are currently in use or booked, or the booking is either active or not paid. You can retire the available ones first before trying again.');
                  }
                  else if (err.status === 500) {
                    alert('An unexpected error occurred. Please try again later.');
                  }
                  else {
                    alert(`Unknown error (${err.status}): ${err.error}`);
                  }
              }
            });
          }
      });
    }

  changePassword(): void {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      return;
    }

    this.userService.changePassword(+userId, this.passwordData).subscribe({
      next: () => {
        alert('Password successfully changed!');
        this.passwordData.oldPassword = '';
        this.passwordData.newPassword = '';
      },
      error: err => {
        console.error('Password update failed:', err);
        if (err.status === 403) {
          alert('Old password is incorrect.');
        } else {
          console.error('Update failed', err);
          alert('An error occurred while changing the password.');
        }
      }
    });
  }

  changeEmail() {
    const userId = localStorage.getItem('userId');
    if (!userId) return;

    const emailValue = this.user.email?.value;
    if (!emailValue) return;

    this.userService.updateEmail(+userId, emailValue).subscribe({
      next: () => {
        alert('Email successfully updated!');
      },
      error: err => {
        if (err.status === 409) {
          alert('This email is already in use.');
        } else {
          console.error('Update failed', err);
          alert('Error while updating email.');
        }
      }
    });
  }

  savePersonalInfo(): void {
    const userId = localStorage.getItem('userId');
    if (!userId) return;

    const personalData = {
      companyName: this.user.companyName,
      phoneNumber: this.user.phoneNumber
    };

    this.userService.updateProviderPersonalInfo(+userId, personalData).subscribe({
      next: () => alert('Personal information updated successfully!'),
      error: err => {
        console.error('Update failed', err);
        alert('Failed to update personal information.');
      }
    });
  }

    saveAddress(): void {
      const userId = localStorage.getItem('userId');
      if (!userId || !this.user?.address) {
        return;
      }

      this.userService.updateAddress(+userId, this.user.address).subscribe({
        next: () => {
          alert('Address updated successfully!');
        },
        error: err => {
          console.error('Address update failed:', err);
          alert('An error occurred while updating the address.');
        }
      });
    }

    savePaymentInfo() {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        return;
      }

      this.userService.updatePaymentInfo(+userId, this.user.paymentinfo).subscribe({
        next: () => alert('Payment info updated successfully!'),
        error: err => {
          console.error('Payment info update failed', err);
          alert('An error occurred while updating payment info.');
        }
      });
    }
}
