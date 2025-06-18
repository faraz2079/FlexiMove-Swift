import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { VehicleService } from 'src/app/src/app/services/vehicle.service';
import { ProviderVehicle } from '../provider-homepage/provider-homepage.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-edit-vehicle-dialog',
  templateUrl: './edit-vehicle-dialog.component.html',
  styleUrls: ['./edit-vehicle-dialog.component.css']
})
export class EditVehicleDialogComponent {
  vehicleForm!: FormGroup;

  licenseMapper: Record<string, string> = {
    MOTORCYCLE: 'A',
    CAR: 'B',
    TRUCK: 'C',
    BUS: 'D',
    NONE: 'NONE'
  };

  constructor(
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: { vehicle: ProviderVehicle },
    private dialogRef: MatDialogRef<EditVehicleDialogComponent>,
    private vehicleService: VehicleService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const v = this.data.vehicle;
    this.vehicleForm = this.fb.group({
      vehicleModel: [v.vehicleModel, Validators.required],
      identificationNumber: [v.identNumber, Validators.required],
      vehicleType: [v.vehicleType, Validators.required],
      billingModel: [v.billingModel, Validators.required],
      priceAmount: [v.priceAmount, Validators.required],
      address: [v.address, Validators.required],
      minAge: [v.restrictions?.minAge, Validators.required],
      maxBookingTimeMinutes: [v.restrictions?.maxBookingTimeMinutes, Validators.required],
      maxDistanceKm: [v.restrictions?.maxDistanceKm, Validators.required],
      maxPassengers: [v.restrictions?.maxPassengers, Validators.required],
      requiredLicenseType: [v.restrictions?.requiredLicense, Validators.required],
      status: [v.status, Validators.required]
    });
  }

  submit(): void {
    if (this.vehicleForm.valid) {
      const formValue = {...this.vehicleForm.value}
      formValue.requiredLicenseType = this.licenseMapper[formValue.requiredLicenseType] || 'NONE';

      this.vehicleService.updateVehicle(this.data.vehicle.vehicleId, formValue).subscribe({
        next: () => {
          this.snackBar.open('Vehicle updated successfully.', 'OK', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: (err) => {
          if (err.status === 404) {
            alert('Vehicle not found.');
          } else if (err.status === 400) {
            alert('Invalid input: ' + err.error);
          } else if (err.status === 409) {
            alert('A vehicle with this identification number already exists.');
          } else {
            alert('Unexpected error occurred.');
          }
        }
      });
    }
  }

  cancel(): void {
    this.dialogRef.close(false);
  }

}
