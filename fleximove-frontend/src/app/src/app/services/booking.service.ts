import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BookingDTO, EndTripRequest, StartTripRequest, TripSummary } from 'src/app/customer-pages/my-bookings/my-bookings.component';
import { CreateBookingRequest } from 'src/app/customer-pages/vehicle-search-result/vehicle-search-result.component';

@Injectable({ providedIn: 'root' })
export class BookingService {
  private baseUrl = 'http://localhost:8083/api/bookings';

  constructor(private http: HttpClient) {}

   createBooking(request: CreateBookingRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/createNewBooking`, request);
  }

  getUserBookings(userId: number): Observable<BookingDTO[]> {
    return this.http.get<BookingDTO[]>(`${this.baseUrl}/user/${userId}`);
  }

  cancelBooking(bookingId: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${bookingId}/cancel`, {});
  }

  startTrip(bookingId: string, request: StartTripRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${bookingId}/start`, request);
  }

  endTrip(bookingId: string, request: EndTripRequest): Observable<TripSummary> {
    return this.http.post<TripSummary>(`${this.baseUrl}/${bookingId}/end`, request);
  }

  payBooking(bookingId: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${bookingId}/pay`, {});
  }

}
