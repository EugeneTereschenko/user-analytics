import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class SupportService {
  private tokenKey = 'auth_token';
  private readonly baseUrl = 'http://localhost:8080/api/support';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  submitFeedback(payload: { subject: string; message: string }) {
    return this.http.post(
      this.baseUrl,
      payload,
      { headers: this.getAuthHeaders() }
    );
  }
}

