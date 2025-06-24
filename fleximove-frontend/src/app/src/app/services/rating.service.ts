import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RatingProviderDTO, RatingVehicleDTO } from 'src/app/customer-pages/my-bookings/my-bookings.component';

@Injectable({ providedIn: 'root' })
export class RatingService {
  private baseUrl = 'http://localhost:8084/ratings';

  constructor(private http: HttpClient) {}

  getProviderRating(providerId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/provider/${providerId}/average`);
  }

  getVehicleRating(vehicleId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/vehicle/${vehicleId}/average`);
  }

  submitVehicleRating(rating: RatingVehicleDTO): Observable<any> {
    return this.http.post(`${this.baseUrl}/vehicle`, rating);
  }

  submitProviderRating(rating: RatingProviderDTO): Observable<any> {
    return this.http.post(`${this.baseUrl}/provider`, rating);
  }

}
