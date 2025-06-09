import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/src/app/services/login.service';
import { UserService } from 'src/app/src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private router: Router, private loginService: LoginService, private snackBar: MatSnackBar, private userService: UserService
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  //TODO: alerts anpassen (mit Snackbar ersetzen)
  //TODO: UserService erstellen
  onSubmit() {
  if (this.loginForm.valid) {
    const formData = this.loginForm.value;
    const payload = {
      email: formData.username.toLowerCase(),
      password: formData.password
    };

    this.loginService.login(payload).subscribe({
      next: user => {
        if (user.role === 'Customer') {
          this.userService.setCustomer(user);
          localStorage.setItem('userId', user.id.toString());
          localStorage.setItem('role', user.role);
          this.router.navigateByUrl('/customer');
        } else if (user.role === 'Provider') {
          //TODO
          //setProvider
          localStorage.setItem('userId', user.id.toString());
          localStorage.setItem('role', user.role);
          //this.router.navigateByUrl('/provider');
        } else {
          this.snackBar.open('Unbekannte Rolle: Zugriff verweigert', 'OK', { panelClass: 'snackbar-error' });
        }},
      error: err => {
        console.error('Login fehlgeschlagen', err);
        alert('Login fehlgeschlagen: Ungültige Zugangsdaten');
      }
    });

  } else {
    this.loginForm.markAllAsTouched();
  }
}

}
