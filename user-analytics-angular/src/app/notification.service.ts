import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly tokenKey = 'auth_token';
  private readonly baseUrl = 'http://localhost:8080/api/notifications';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl, {
      headers: this.getAuthHeaders()
    });
  }
}
