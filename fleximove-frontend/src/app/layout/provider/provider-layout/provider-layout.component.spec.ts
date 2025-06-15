import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProviderLayoutComponent } from './provider-layout.component';

describe('ProviderLayoutComponent', () => {
  let component: ProviderLayoutComponent;
  let fixture: ComponentFixture<ProviderLayoutComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProviderLayoutComponent]
    });
    fixture = TestBed.createComponent(ProviderLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
