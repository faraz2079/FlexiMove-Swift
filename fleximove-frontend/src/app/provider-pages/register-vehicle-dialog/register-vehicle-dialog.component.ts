import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
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
      identificationNumber: [''],
      vehicleType: [''],
      vehicleModel: [''],
      priceAmount: [0],
      billingModel: [''],
      address: [''],
      minAge: [null],
      maxBookingTimeMinutes: [null],
      maxDistanceKm: [null],
      maxPassengers: [null],
      requiredLicenseType: ['']
    });
  }

  submit(): void {
    if (this.vehicleForm.valid) {
      this.vehicleService.registerVehicle(this.vehicleForm.value, this.data.providerId).subscribe({
        next: () => this.dialogRef.close(true),
        error: () => alert('Fehler beim Registrieren des Fahrzeugs.')
      });
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
