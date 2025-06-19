import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { VehicleService } from 'src/app/src/app/services/vehicle.service';

@Component({
  selector: 'app-register-vehicle-dialog',
  templateUrl: './register-vehicle-dialog.component.html',
  styleUrls: ['./register-vehicle-dialog.component.css']
})
export class RegisterVehicleDialogComponent {
  vehicleForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<RegisterVehicleDialogComponent>,
    private vehicleService: VehicleService,
    @Inject(MAT_DIALOG_DATA) public data: { providerId: number }
  ) {
    this.vehicleForm = this.fb.group({
      identificationNumber: ['', Validators.required],
      vehicleType: ['', Validators.required],
      vehicleModel: ['', Validators.required],
      priceAmount: [0, Validators.required],
      billingModel: ['', Validators.required],
      address: ['', Validators.required],
      minAge: [null, Validators.required],
      maxBookingTimeMinutes: [null, Validators.required],
      maxDistanceKm: [null, Validators.required],
      maxPassengers: [null, Validators.required],
      requiredLicenseType: ['', Validators.required]
    });
  }

  submit(): void {
    if (this.vehicleForm.valid) {
      this.vehicleService.registerVehicle(this.vehicleForm.value, this.data.providerId).subscribe({
        next: () => this.dialogRef.close(true),
        error: (err) => {
        if (err.status === 409) {
          alert('A vehicle with this identification number already exists.');
        } else if (err.status === 400) {
          alert(err.error); // zeigt z. B. "A valid driver license is required for vehicle type 'CAR'..."
        } else {
          alert('An unexpected error occurred while registering the vehicle.');
        }
      }
      });
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
