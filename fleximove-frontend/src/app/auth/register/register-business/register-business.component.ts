import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Provider } from 'src/app/models/provider.model';
import { RegistrationService } from 'src/app/src/app/services/registration.service';
import { passwordStrengthValidator } from 'src/app/validators/password-strength.validator';

@Component({
  selector: 'app-register-business',
  templateUrl: './register-business.component.html',
  styleUrls: ['./register-business.component.css']
})
export class RegisterBusinessComponent {
  businessForm: FormGroup;

  constructor(private fb: FormBuilder, private registrationService: RegistrationService, private snackBar: MatSnackBar) {
    this.businessForm = this.fb.group({
      companyName: ['', Validators.required],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      postalCode: ['', Validators.required],
      city: ['', Validators.required],
      country: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordStrengthValidator]],
      paymentinfo: this.fb.group({
        cardHolderName: [''],
        creditCardNumber: [''],
        cvv: [''],
        expiryDate: [''],
      })
    });
  }

  onSubmit() {
    if (this.businessForm.valid) {
      const formData = this.businessForm.value;
    
      const provider: Provider = {
        phoneNumber: formData.phone,
        email: formData.email.toLowerCase(),
        password: formData.password,
        address: {
          street: formData.address,
          city: formData.city,
          postcode: formData.postalCode,
          country: formData.country
        },
        companyName: formData.companyName,
        paymentinfo: {
          cardHolderName: formData.paymentinfo.cardHolderName,
          creditCardNumber: formData.paymentinfo.creditCardNumber,
          cvv: formData.paymentinfo.cvv,
          expiryDate: formData.paymentinfo.expiryDate
        }
      };
  
      console.log(provider)
      this.registrationService.registerProvider(provider).subscribe({
        next: (res: string) => {
          this.snackBar.open(res, 'OK', {
          duration: 3000,
          panelClass: ['snackbar-success']
        });
        },
        error: err => {
          this.snackBar.open('Fehler: ' + err.error, 'OK', {
          duration: 5000,
          panelClass: ['snackbar-error']
        });
    }
      });
    } else {
      this.businessForm.markAllAsTouched();
    }
  }
}
