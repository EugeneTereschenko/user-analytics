import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AnalyticsService {
  constructor(private http: HttpClient) {}

  getSummary() {
    return this.http.get('/api/analytics/users/summary');
  }

  getSignups() {
    return this.http.get<number[]>('/api/analytics/users/signups?start=2025-06-01&end=2025-06-05');
  }

  getDevices() {
    return this.http.get('/api/analytics/users/devices');
  }
}
