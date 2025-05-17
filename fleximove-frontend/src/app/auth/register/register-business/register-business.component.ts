import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { passwordStrengthValidator } from 'src/app/validators/password-strength.validator';

@Component({
  selector: 'app-register-business',
  templateUrl: './register-business.component.html',
  styleUrls: ['./register-business.component.css']
})
export class RegisterBusinessComponent {
  businessForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.businessForm = this.fb.group({
      companyName: ['', Validators.required],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      postalCode: ['', Validators.required],
      city: ['', Validators.required],
      country: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordStrengthValidator]]
    });
  }

  onSubmit() {
    if (this.businessForm.valid) {
      console.log('Business Registration:', this.businessForm.value);
    } else {
      this.businessForm.markAllAsTouched();
    }
  }
}
