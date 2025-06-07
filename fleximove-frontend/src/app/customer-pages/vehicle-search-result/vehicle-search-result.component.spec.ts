import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleSearchResultComponent } from './vehicle-search-result.component';

describe('VehicleSearchResultComponent', () => {
  let component: VehicleSearchResultComponent;
  let fixture: ComponentFixture<VehicleSearchResultComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VehicleSearchResultComponent]
    });
    fixture = TestBed.createComponent(VehicleSearchResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
