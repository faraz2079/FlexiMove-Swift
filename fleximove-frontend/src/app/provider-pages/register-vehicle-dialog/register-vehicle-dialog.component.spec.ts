import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterVehicleDialogComponent } from './register-vehicle-dialog.component';

describe('RegisterVehicleDialogComponent', () => {
  let component: RegisterVehicleDialogComponent;
  let fixture: ComponentFixture<RegisterVehicleDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegisterVehicleDialogComponent]
    });
    fixture = TestBed.createComponent(RegisterVehicleDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
