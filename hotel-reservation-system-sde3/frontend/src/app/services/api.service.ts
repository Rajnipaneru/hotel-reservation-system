import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { BookingResponse } from '../models/booking-response.model';
import { Observable } from 'rxjs/internal/Observable';
import { Room } from '../models/room.model';


@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

 bookRooms(request: any): Observable<BookingResponse> {
  return this.http.post<BookingResponse>(
    `${this.apiUrl}/reservations/book`,
    request
  );
}
getBookedRooms(): Observable<any> {
  return this.http.get(`${this.apiUrl}/rooms/booked`);
}
  getStats() {
    return this.http.get(`${this.apiUrl}/rooms/stats`);
  }

  getAvailableRooms(): Observable<Room[]> {
  return this.http.get<Room[]>(`${this.apiUrl}/rooms/available`);
}
  initializeRooms(): Observable<string> {
  return this.http.post(
    `${this.apiUrl}/rooms/init`,
    {},
    { responseType: 'text' }
  );
}

  resetRooms(): Observable<string> {
  return this.http.post(
    `${this.apiUrl}/rooms/reset`,
    {},
    { responseType: 'text' }
  );
}

}