import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CalendarEvent {
  title: string;
  date: string;
}

@Injectable({
  providedIn: 'root'
})
export class CalendarService {

  private readonly tokenKey = 'auth_token';
  private readonly baseUrl = 'http://localhost:8080/api/calendar';

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  saveEvent(event: CalendarEvent): Observable<any> {
    return this.http.post(`${this.baseUrl}/events`, event, {
      headers: this.getAuthHeaders()
    });
  }

  getAllEvents(): Observable<CalendarEvent[]> {
    return this.http.get<CalendarEvent[]>(`${this.baseUrl}/events`, {
      headers: this.getAuthHeaders()
    });
  }
}