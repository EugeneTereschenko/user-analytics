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
  constructor(private http: HttpClient) {}

  getSummary(): Observable<SummaryApiResponse>  {
    return this.http.get<SummaryApiResponse>('http://localhost:8080/api/analytics/users/summary');
  }

  getSignups() {
    return this.http.get<number[]>('http://localhost:8080/api/analytics/users/signups?start=2025-06-01&end=2025-06-05');
  }

  getDevices() {
    return this.http.get('http://localhost:8080/api/analytics/users/devices');
  }

  getSummaryDate(startDate?: string, endDate?: string): Observable<any> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);

    return this.http.get('/api/analytics/summary', { params });
  }
}
