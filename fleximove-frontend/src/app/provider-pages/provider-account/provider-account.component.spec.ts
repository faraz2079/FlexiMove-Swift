import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProviderAccountComponent } from './provider-account.component';

describe('ProviderAccountComponent', () => {
  let component: ProviderAccountComponent;
  let fixture: ComponentFixture<ProviderAccountComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProviderAccountComponent]
    });
    fixture = TestBed.createComponent(ProviderAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
