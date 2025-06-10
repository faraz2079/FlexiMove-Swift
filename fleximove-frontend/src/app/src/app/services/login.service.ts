import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LoginService {
  private baseUrl = 'http://localhost:8081/api/users';

  constructor(private http: HttpClient) {}

  login(payload: { email: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, payload);
  }
}

