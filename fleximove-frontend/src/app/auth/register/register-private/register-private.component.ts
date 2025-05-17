import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { passwordStrengthValidator } from 'src/app/validators/password-strength.validator';

@Component({
  selector: 'app-register-private',
  templateUrl: './register-private.component.html',
  styleUrls: ['./register-private.component.css']
})
export class RegisterPrivateComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      dob: ['', Validators.required],
      phone: [''],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordStrengthValidator]],
      driverLicense: [null] // optional file upload
    });
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.registerForm.patchValue({ driverLicense: file });
    }
  }

  onSubmit() {
    if (this.registerForm.valid) {
      console.log('Form submitted:', this.registerForm.value);
    } else {
      this.registerForm.markAllAsTouched();
    }
  }
}
