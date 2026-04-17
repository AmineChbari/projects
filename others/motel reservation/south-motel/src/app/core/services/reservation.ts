import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reservation } from '../models/reservation.model';

@Injectable({ providedIn: 'root' })
export class ReservationService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.apiUrl);
  }

  create(data: Omit<Reservation, 'id'>): Observable<Reservation> {
    return this.http.post<Reservation>(this.apiUrl, data);
  }

  update(id: number, data: Omit<Reservation, 'id'>): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.apiUrl}?id=${id}`, data);
  }

  delete(id: number): Observable<{ success: boolean }> {
    return this.http.delete<{ success: boolean }>(`${this.apiUrl}?id=${id}`);
  }
}
