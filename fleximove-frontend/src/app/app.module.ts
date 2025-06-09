import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './auth/login/login.component';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import { AccountTypeSelectorComponent } from './auth/register/account-type-selector.component';
import { RegisterPrivateComponent } from './auth/register/register-private/register-private.component';
import { RegisterBusinessComponent } from './auth/register/register-business/register-business.component';
import { RouterModule } from '@angular/router';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { CustomerLayoutComponent } from './layout/customer/customer-layout/customer-layout.component';
import { CustomerHeaderComponent } from './layout/customer/customer-header/customer-header.component';
import { FooterComponent } from './layout/footer/footer.component';
import {MatIconModule} from '@angular/material/icon';
import { CustomerHomepageComponent } from './customer-pages/customer-homepage/customer-homepage.component';
import {MatSelectModule} from '@angular/material/select';
import {FormsModule} from '@angular/forms';
import { RegistrationService } from './src/app/services/registration.service';
import { HttpClientModule } from '@angular/common/http';
import {MatSnackBarModule } from '@angular/material/snack-bar';
import { LoginService } from './src/app/services/login.service';
import { GeocodingService } from './src/app/services/geocoding.service';
import { VehicleService } from './src/app/services/vehicle.service';
import { VehicleSearchResultComponent } from './customer-pages/vehicle-search-result/vehicle-search-result.component';
import { VehicleMapComponent } from './vehicle-map/vehicle-map.component';
import { RatingModule } from 'primeng/rating';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CustomerAccountComponent } from './customer-pages/customer-account/customer-account.component';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDialogModule } from '@angular/material/dialog';
import { DeleteAccountDialogComponent } from './customer-pages/delete-account-dialog/delete-account-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AccountTypeSelectorComponent,
    RegisterPrivateComponent,
    RegisterBusinessComponent,
    CustomerLayoutComponent,
    CustomerHeaderComponent,
    FooterComponent,
    CustomerHomepageComponent,
    VehicleSearchResultComponent,
    VehicleMapComponent,
    CustomerAccountComponent,
    DeleteAccountDialogComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
    MatSelectModule,
    FormsModule,
    HttpClientModule,
    MatSnackBarModule,
    MatAutocompleteModule,
    RatingModule,
    MatTooltipModule,
    MatTabsModule,
    MatDialogModule
  ],
  providers: [RegistrationService, LoginService, GeocodingService, VehicleService],
  bootstrap: [AppComponent]
})
export class AppModule { }
