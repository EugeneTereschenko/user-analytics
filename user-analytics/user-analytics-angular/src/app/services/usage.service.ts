
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject, tap, catchError, of } from 'rxjs';


export interface FeatureUsageDTO {
  features: string[];
  usageCounts: number[];
  timestamps?: string[];
  categories?: string[];
  avgTimeSpent?: number[];
  growthRate?: number[];
  ratings?: number[];
  activeUsers?: number[];
}

export interface UsageStatistics {
  totalUsers: number;
  activeUsers: number;
  engagementRate: number;
  avgRating: number;
  totalSessions: number;
  avgSessionDuration: string;
}

export interface FeatureDetail {
  name: string;
  category: string;
  users: number;
  avgTime: string;
  growth: number;
  rating: number;
  sessions: number;
  lastUsed?: string;
}

export interface TimeSeriesData {
  date: string;
  value: number;
  feature?: string;
}

export interface UsageTrend {
  period: 'daily' | 'weekly' | 'monthly' | 'yearly';
  data: TimeSeriesData[];
}


@Injectable({
  providedIn: 'root'
})
export class UsageService {
  private readonly tokenKey = 'auth_token';
  private readonly baseUrl = 'http://localhost:8080/api/usage';
  
  private statistics$ = new BehaviorSubject<UsageStatistics | null>(null);
  private isLoading$ = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  /**
   * Get feature usage statistics
   */
  getFeatureUsage(period: string = 'weekly'): Observable<FeatureUsageDTO> {
    const params = new HttpParams().set('period', period);
    return this.http.get<FeatureUsageDTO>(`${this.baseUrl}/feature`, { 
      headers: this.getAuthHeaders(),
      params 
    }).pipe(
      catchError(error => {
        console.error('Error fetching feature usage:', error);
        return of({
          features: [],
          usageCounts: []
        });
      })
    );
  }

  /**
   * Get overall usage statistics
   */
  getStatistics(): Observable<UsageStatistics> {
    return this.http.get<UsageStatistics>(`${this.baseUrl}/statistics`, { 
      headers: this.getAuthHeaders() 
    }).pipe(
      tap(stats => this.statistics$.next(stats)),
      catchError(error => {
        console.error('Error fetching statistics:', error);
        return of({
          totalUsers: 0,
          activeUsers: 0,
          engagementRate: 0,
          avgRating: 0,
          totalSessions: 0,
          avgSessionDuration: '0m 0s'
        });
      })
    );
  }

  /**
   * Get detailed feature metrics
   */
  getFeatureDetails(): Observable<FeatureDetail[]> {
    return this.http.get<FeatureDetail[]>(`${this.baseUrl}/feature/details`, { 
      headers: this.getAuthHeaders() 
    });
  }

  /**
   * Get usage trends over time
   */
  getUsageTrends(period: string = 'weekly', feature?: string): Observable<UsageTrend> {
    let params = new HttpParams().set('period', period);
    if (feature) {
      params = params.set('feature', feature);
    }
    return this.http.get<UsageTrend>(`${this.baseUrl}/trends`, { 
      headers: this.getAuthHeaders(),
      params 
    });
  }

  /**
   * Get top features by usage
   */
  getTopFeatures(limit: number = 5): Observable<FeatureDetail[]> {
    const params = new HttpParams().set('limit', limit.toString());
    return this.http.get<FeatureDetail[]>(`${this.baseUrl}/feature/top`, { 
      headers: this.getAuthHeaders(),
      params 
    });
  }

  /**
   * Get usage by category
   */
  getUsageByCategory(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/category`, { 
      headers: this.getAuthHeaders() 
    });
  }

  /**
   * Get user engagement metrics
   */
  getEngagementMetrics(period: string = 'weekly'): Observable<any> {
    const params = new HttpParams().set('period', period);
    return this.http.get<any>(`${this.baseUrl}/engagement`, { 
      headers: this.getAuthHeaders(),
      params 
    });
  }

  /**
   * Track feature usage (to be called when user uses a feature)
   */
  trackFeatureUsage(featureName: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/track`, { 
      featureName,
      timestamp: new Date().toISOString()
    }, { headers: this.getAuthHeaders() });
  }

  /**
   * Get statistics observable
   */
  getStatisticsObservable(): Observable<UsageStatistics | null> {
    return this.statistics$.asObservable();
  }

  /**
   * Get loading state
   */
  isLoading(): Observable<boolean> {
    return this.isLoading$.asObservable();
  }

  /**
   * Export usage data
   */
  exportUsageData(format: 'json' | 'csv' = 'json'): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/export`, {
      headers: this.getAuthHeaders(),
      params: new HttpParams().set('format', format),
      responseType: 'blob'
    });
  }
}
