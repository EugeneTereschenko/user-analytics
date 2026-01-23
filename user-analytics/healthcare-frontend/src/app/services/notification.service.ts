import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Notification } from '../models/notification.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private readonly API_URL = 'http://localhost:8080/api/notifications';
  private http = inject(HttpClient);

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getNotificationsByRecipient(recipientId: number): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.API_URL}/recipient/${recipientId}`, { headers: this.getHeaders() });
  }

  getNotificationById(id: number): Observable<Notification> {
    return this.http.get<Notification>(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
  }

  createNotification(notification: Notification): Observable<Notification> {
    return this.http.post<Notification>(this.API_URL, notification, { headers: this.getHeaders() });
  }

  cancelNotification(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
  }

  processScheduled(): Observable<any> {
    return this.http.post(`${this.API_URL}/process-scheduled`, {}, { headers: this.getHeaders() });
  }

  retryFailed(): Observable<any> {
    return this.http.post(`${this.API_URL}/retry-failed`, {}, { headers: this.getHeaders() });
  }
}
