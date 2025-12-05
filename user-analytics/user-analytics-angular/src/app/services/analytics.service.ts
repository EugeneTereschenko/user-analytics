import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SummaryApiResponse {
  totalUsers: number;
  activeUsers: number;
  newUsersToday?: number;
  bounceRate?: number;
}

@Injectable({ providedIn: 'root' })
export class AnalyticsService {
  private readonly tokenKey = 'auth_token';
  private readonly baseUrl = 'http://localhost:8080/api/analytics/';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): { [header: string]: string } | undefined {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { 'Authorization': `Bearer ${token}` } : undefined;
  }

  getSummary(): Observable<SummaryApiResponse> {
    return this.http.get<SummaryApiResponse>(
      `${this.baseUrl}users/summary`,
      { headers: this.getAuthHeaders() }
    );
  }

  getSignups(start?: string, end?: string): Observable<number[]> {
    let params = new HttpParams();
    if (start) params = params.set('start', start);
    if (end) params = params.set('end', end);

    return this.http.get<number[]>(
      `${this.baseUrl}users/signups`,
      { headers: this.getAuthHeaders(), params }
    );
  }

  getDevices(): Observable<any> {
    return this.http.get(
      `${this.baseUrl}users/devices`,
      { headers: this.getAuthHeaders() }
    );
  }

  getSummaryDate(startDate?: string, endDate?: string): Observable<any> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);

    return this.http.get(
      `${this.baseUrl}users/summary`,
      { headers: this.getAuthHeaders(), params }
    );
  }

  getLocations(): Observable<Map<string, number>> {
    return this.http.get<Map<string, number>>(
      `${this.baseUrl}users/locations`,
      { headers: this.getAuthHeaders() }
    );
  }
}
