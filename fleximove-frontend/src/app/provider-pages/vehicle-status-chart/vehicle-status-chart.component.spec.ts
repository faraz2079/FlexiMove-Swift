import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleStatusChartComponent } from './vehicle-status-chart.component';

describe('VehicleStatusChartComponent', () => {
  let component: VehicleStatusChartComponent;
  let fixture: ComponentFixture<VehicleStatusChartComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VehicleStatusChartComponent]
    });
    fixture = TestBed.createComponent(VehicleStatusChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
