import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NearestAvailableVehicleResponse } from 'src/app/customer-pages/customer-homepage/customer-homepage.component';
import { ProviderVehicle} from 'src/app/provider-pages/provider-homepage/provider-homepage.component';

@Injectable({ providedIn: 'root' })
export class VehicleService {
  private baseUrl = 'http://localhost:8085/vehicles';
  private results: NearestAvailableVehicleResponse[] = [];

  constructor(private http: HttpClient) {}

  getNearbyVehicles(address: string, radius?: number): Observable<NearestAvailableVehicleResponse[]> {
    let params = new HttpParams().set('address', address);
    if (radius !== undefined) {
        params = params.set('radiusInKm', radius);
    }
    return this.http.get<any[]>(`${this.baseUrl}/nearby`, { params });
  }

  getVehicleById(id: number): Observable<any> {
      return this.http.get<any[]>(`${this.baseUrl}/load/${id}`);
  }

  getVehiclesByProvider(providerId: number): Observable<ProviderVehicle[]> {
    const params = new HttpParams().set('forProviderId', providerId);
    return this.http.get<ProviderVehicle[]>(`${this.baseUrl}/providerVehiclesList`, { params });
  }

  registerVehicle(request: any, providerId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/registeredBy?providerId=${providerId}`, request);
  }

  setResults(vehicles: NearestAvailableVehicleResponse[]) {
    this.results = vehicles;
  }

  getResults(): NearestAvailableVehicleResponse[] {
    return this.results;
  }

  clear() {
    this.results = [];
  }
}
