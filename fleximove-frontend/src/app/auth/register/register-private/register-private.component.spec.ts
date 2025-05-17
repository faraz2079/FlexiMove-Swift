import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterPrivateComponent } from './register-private.component';

describe('RegisterPrivateComponent', () => {
  let component: RegisterPrivateComponent;
  let fixture: ComponentFixture<RegisterPrivateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegisterPrivateComponent]
    });
    fixture = TestBed.createComponent(RegisterPrivateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
