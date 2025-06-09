import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Customer } from "src/app/models/customer.model";

@Injectable({ providedIn: 'root' })
export class UserService {
    private baseUrl = 'http://localhost:8081/api/users';
    private customer: Customer | null = null; 

    constructor(private http: HttpClient) {}

    getCustomerById(userId: string): Observable<Customer> {
        return this.http.get<Customer>(`${this.baseUrl}/${userId}`);
    }

    setCustomer(customer: Customer) {
        this.customer = customer;
      }
    
      getCustomer(): Customer | null {
        return this.customer;
      }
}
