import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from 'src/app/models/customer.model';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private baseUrl = 'http://localhost:8081/api/users';

  constructor(private http: HttpClient) { }

  registerCustomer(customerData: Customer): Observable<any> {
    return this.http.post(`${this.baseUrl}/registercustomer`, customerData, {responseType: 'text' as 'json'});
  }

  //TODO
  registerProvider(providerData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/registerprovider`, providerData);
  }
}
