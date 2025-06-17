import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProviderHomepageComponent } from './provider-homepage.component';

describe('ProviderHomepageComponent', () => {
  let component: ProviderHomepageComponent;
  let fixture: ComponentFixture<ProviderHomepageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProviderHomepageComponent]
    });
    fixture = TestBed.createComponent(ProviderHomepageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
