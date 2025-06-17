import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProviderHeaderComponent } from './provider-header.component';

describe('ProviderHeaderComponent', () => {
  let component: ProviderHeaderComponent;
  let fixture: ComponentFixture<ProviderHeaderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProviderHeaderComponent]
    });
    fixture = TestBed.createComponent(ProviderHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
