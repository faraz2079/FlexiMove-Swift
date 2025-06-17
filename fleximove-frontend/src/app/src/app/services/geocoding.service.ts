import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GeocodingService {
  private readonly API_KEY = '682a05be63f3b537943526zbg7bed1c';
  private readonly REVERSE_GEOCODE_URL = 'https://geocode.maps.co/reverse';

  constructor(private http: HttpClient) {}

  reverseGeocode(lat: number, lon: number): Observable<string> {
    const url = `${this.REVERSE_GEOCODE_URL}?lat=${lat}&lon=${lon}&api_key=${this.API_KEY}`;

    return this.http.get<any>(url).pipe(
      map(response => response.display_name || 'Adresse nicht gefunden')
    );
  }
}
