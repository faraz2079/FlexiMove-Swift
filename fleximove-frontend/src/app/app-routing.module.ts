import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { AccountTypeSelectorComponent } from './auth/register/account-type-selector.component';
import { RegisterPrivateComponent } from './auth/register/register-private/register-private.component';
import { RegisterBusinessComponent } from './auth/register/register-business/register-business.component';
import { CustomerLayoutComponent } from './layout/customer/customer-layout/customer-layout.component';
import { CustomerHomepageComponent } from './customer-pages/customer-homepage/customer-homepage.component';
import { VehicleSearchResultComponent } from './customer-pages/vehicle-search-result/vehicle-search-result.component';
import { CustomerAccountComponent } from './customer-pages/customer-account/customer-account.component';
import { ProviderLayoutComponent } from './layout/provider/provider-layout/provider-layout.component';
import { ProviderHomepageComponent } from './provider-pages/provider-homepage/provider-homepage.component';

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
  {path: 'customer', component: CustomerLayoutComponent,
    children: [
      { path: '', component: CustomerHomepageComponent },
      { path: 'home', component: CustomerHomepageComponent },
      { path: 'search-results', component: VehicleSearchResultComponent },
      { path: 'account', component: CustomerAccountComponent }
    ]
  },
  {path: 'provider', component: ProviderLayoutComponent,
    children: [
      { path: '', component: ProviderHomepageComponent },
      { path: 'home', component: ProviderHomepageComponent },
      //{ path: 'account', component: CustomerAccountComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
