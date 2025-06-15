import { HttpClient } from "@angular/common/http";
import { Injectable} from "@angular/core";
import { Observable } from "rxjs";
import { Address, Customer, PaymentInfo } from "src/app/models/customer.model";
import { Provider as FlexiProvider} from "src/app/models/provider.model";

@Injectable({ providedIn: 'root' })
export class UserService {
    private baseUrl = 'http://localhost:8081/api/users';
    private customer: Customer | null = null; 
    private provider: FlexiProvider | null = null; 

    constructor(private http: HttpClient) {}

    getUserById(userId: string): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/${userId}`);
    }

    deleteAccount(userId: number) {
      return this.http.delete(`${this.baseUrl}/deleteAccount/${userId}`);
    }

    changePassword(userId: number, passwordData: { oldPassword: string; newPassword: string }) {
      return this.http.put(`${this.baseUrl}/${userId}/password`, passwordData);
    }

    updateEmail(userId: number, newEmail: string) {
      return this.http.put(`${this.baseUrl}/${userId}/email`, newEmail);
    }

    updatePersonalInfo(userId: number, personalData: any) {
      return this.http.put(`${this.baseUrl}/${userId}/personal-info`, personalData);
    }

    updateAddress(userId: number, newAddress: Address) {
      return this.http.put(`${this.baseUrl}/${userId}/address`, newAddress);
    }

    updatePaymentInfo(userId: number, paymentInfo: PaymentInfo) {
      return this.http.put<void>(`${this.baseUrl}/${userId}/payment-info`, paymentInfo);
    }

    setCustomer(customer: Customer) {
      this.customer = customer;
    }
    
    getCustomer(): Customer | null {
      return this.customer;
    }

    setProvider(provider: FlexiProvider) {
      this.provider = provider;
    }
    
    getProvider(): FlexiProvider | null {
      return this.provider;
    }
}
