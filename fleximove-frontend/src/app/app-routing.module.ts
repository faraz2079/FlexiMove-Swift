import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { AccountTypeSelectorComponent } from './auth/register/account-type-selector.component';
import { RegisterPrivateComponent } from './auth/register/register-private/register-private.component';
import { RegisterBusinessComponent } from './auth/register/register-business/register-business.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'register',
    children: [
      { path: '', component: AccountTypeSelectorComponent },
      { path: 'private', component: RegisterPrivateComponent },
      { path: 'business', component: RegisterBusinessComponent }
    ]
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
