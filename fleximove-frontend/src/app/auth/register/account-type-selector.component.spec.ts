import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountTypeSelectorComponent } from './account-type-selector.component';

describe('RegisterComponent', () => {
  let component: AccountTypeSelectorComponent;
  let fixture: ComponentFixture<AccountTypeSelectorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountTypeSelectorComponent]
    });
    fixture = TestBed.createComponent(AccountTypeSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
