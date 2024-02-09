import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private baseUrl = 'http://localhost:8080/v1/customer'
  constructor(private http: HttpClient) { }
  createCustomer(customer: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/signup`, customer);
  }
  sendVerificationCode(phoneNumber: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/verify?phoneNumber=${encodeURIComponent(phoneNumber)}`, {})
  }
  verifyPhoneNumber(phoneNumber: string, code: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/verify/confirm?phoneNumber=${encodeURIComponent(phoneNumber)}&code=${code}`, {});
  }
}
