import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RatingService {
  private baseUrl = 'http://localhost:8084/ratings';

  constructor(private http: HttpClient) {}

  getProviderRating(providerId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/provider/${providerId}/average`);
    }

}
