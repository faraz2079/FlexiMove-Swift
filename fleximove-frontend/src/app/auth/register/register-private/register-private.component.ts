import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { passwordStrengthValidator } from 'src/app/validators/password-strength.validator';
import { Customer } from 'src/app/models/customer.model';
import { RegistrationService } from 'src/app/src/app/services/registration.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-register-private',
  templateUrl: './register-private.component.html',
  styleUrls: ['./register-private.component.css']
})
export class RegisterPrivateComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private registrationService: RegistrationService, private snackBar: MatSnackBar) {
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      phone: [''],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordStrengthValidator]],
      driverLicense: [null],
      driverLicenseType: ['NONE', Validators.required],
      address: this.fb.group({
        street: [''],
        city: [''],
        postcode: [''],
        country: [''],
      }),
      paymentinfo: this.fb.group({
        cardHolderName: ['', Validators.required],
        creditCardNumber: ['', Validators.required],
        cvv: ['', Validators.required],
        expiryDate: ['', Validators.required],
      })
    });

  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.registerForm.patchValue({ driverLicense: file });
    }
  }

  //TODO for future: die Werte sollen auch beim Neuladen der Seite in Form bleiben
  //TODO for future: die Daten validieren, wenn der User die eingibt (Eventuell im Backend auch Validierung machen)

onSubmit() {
  if (this.registerForm.valid) {
    const formData = this.registerForm.value;

    const isoDateOfBirth = formData.dateOfBirth.toISOString().split('T')[0]; // YYYY-MM-DD

    const customer: Customer = {
      firstName: formData.firstName,
      lastName: formData.lastName,
      dateOfBirth: isoDateOfBirth,
      phoneNumber: formData.phone,
      email: formData.email.toLowerCase(),
      password: formData.password,
      driverLicenseType: formData.driverLicenseType || 'NONE',
      address: {
        street: formData.address.street,
        city: formData.address.city,
        postcode: formData.address.postcode,
        country: formData.address.country
      },
      paymentinfo: {
        cardHolderName: formData.paymentinfo.cardHolderName,
        creditCardNumber: formData.paymentinfo.creditCardNumber,
        cvv: formData.paymentinfo.cvv,
        expiryDate: formData.paymentinfo.expiryDate
      }
    };

    this.registrationService.registerCustomer(customer).subscribe({
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
    this.registerForm.markAllAsTouched();
  }
}


}
