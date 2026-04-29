import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {
    // For production
    if (typeof window !== 'undefined' && window.location.hostname !== 'localhost') {
      this.apiUrl = 'https://hotel-reservation-backend.onrender.com/api';
    }
  }

  bookRooms(request: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/reservations/book`, request);
  }

  getStats(): Observable<any> {
    return this.http.get(`${this.apiUrl}/rooms/stats`);
  }

  getAvailableRooms(): Observable<any> {
    return this.http.get(`${this.apiUrl}/rooms/available`);
  }

  initializeRooms(): Observable<any> {
    return this.http.post(`${this.apiUrl}/rooms/init`, {});
  }

  resetRooms(): Observable<any> {
    return this.http.post(`${this.apiUrl}/rooms/reset`, {});
  }
}