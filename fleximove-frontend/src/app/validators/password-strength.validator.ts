import { AbstractControl, ValidationErrors } from '@angular/forms';

export function passwordStrengthValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value;
  if (!value) return null;

  const errors: any = {};
  if (value.length < 8) errors.minLength = true;
  if (!/[a-zA-Z]/.test(value)) errors.letter = true;
  if (!/[0-9]/.test(value)) errors.number = true;
  if (/password/i.test(value)) errors.containsPassword = true;

  return Object.keys(errors).length ? errors : null;
}
