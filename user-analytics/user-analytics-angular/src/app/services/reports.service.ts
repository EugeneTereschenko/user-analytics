import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap, catchError, of } from 'rxjs';


export interface ReportTemplate {
  id: string;
  name: string;
  description: string;
  icon: string;
  category: 'user' | 'activity' | 'usage' | 'system' | 'financial';
  fields: ReportField[];
}

export interface ReportField {
  key: string;
  label: string;
  type: 'text' | 'number' | 'date' | 'select' | 'boolean';
  required: boolean;
  options?: string[];
}

export interface ReportFilter {
  startDate?: string;
  endDate?: string;
  userType?: string;
  status?: string;
  category?: string;
  searchQuery?: string;
}

export interface ReportData {
  id?: string;
  title: string;
  type: string;
  generatedAt: string;
  generatedBy?: string;
  data: any[];
  summary?: ReportSummary;
  charts?: ChartData[];
}

export interface ReportSummary {
  totalRecords: number;
  totalValue?: number;
  averageValue?: number;
  trends?: TrendData[];
  highlights?: string[];
}

export interface ChartData {
  type: 'bar' | 'line' | 'pie' | 'doughnut';
  labels: string[];
  datasets: any[];
}

export interface TrendData {
  label: string;
  value: number;
  change: number;
  changeType: 'increase' | 'decrease' | 'stable';
}

export interface ScheduledReport {
  id?: string;
  name: string;
  templateId: string;
  frequency: 'daily' | 'weekly' | 'monthly';
  recipients: string[];
  filters: ReportFilter;
  enabled: boolean;
  nextRun?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ReportsService {
  private readonly baseUrl = 'http://localhost:8080/api/reports';
  private readonly tokenKey = 'auth_token';
  
  private availableReports$ = new BehaviorSubject<ReportTemplate[]>([]);
  private isGenerating$ = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient) {
    this.loadAvailableReports();
  }

  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  /**
   * Get all available report templates
   */
  getReportTemplates(): Observable<ReportTemplate[]> {
    return this.http.get<ReportTemplate[]>(`${this.baseUrl}/templates`, { 
      headers: this.getAuthHeaders() 
    }).pipe(
      tap(templates => this.availableReports$.next(templates)),
      catchError(error => {
        console.error('Error fetching report templates:', error);
        return of([]);
      })
    );
  }

  /**
   * Generate a report
   */
  generateReport(templateId: string, filters: ReportFilter): Observable<ReportData> {
    this.isGenerating$.next(true);
    
    return this.http.post<ReportData>(`${this.baseUrl}/generate`, {
      templateId,
      filters
    }, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.isGenerating$.next(false)),
      catchError(error => {
        console.error('Error generating report:', error);
        this.isGenerating$.next(false);
        throw error;
      })
    );
  }

  /**
   * Get user activity report
   */
  getUserActivityReport(filters: ReportFilter): Observable<ReportData> {
    let params = new HttpParams();
    if (filters.startDate) params = params.set('startDate', filters.startDate);
    if (filters.endDate) params = params.set('endDate', filters.endDate);
    
    return this.http.get<ReportData>(`${this.baseUrl}/user-activity`, { 
      headers: this.getAuthHeaders(),
      params 
    });
  }

  /**
   * Get system usage report
   */
  getSystemUsageReport(filters: ReportFilter): Observable<ReportData> {
    let params = new HttpParams();
    if (filters.startDate) params = params.set('startDate', filters.startDate);
    if (filters.endDate) params = params.set('endDate', filters.endDate);
    
    return this.http.get<ReportData>(`${this.baseUrl}/system-usage`, { 
      headers: this.getAuthHeaders(),
      params 
    });
  }

  /**
   * Get audit log report
   */
  getAuditLogReport(filters: ReportFilter): Observable<ReportData> {
    return this.http.post<ReportData>(`${this.baseUrl}/audit-logs`, filters, { 
      headers: this.getAuthHeaders() 
    });
  }

  /**
   * Get performance metrics report
   */
  getPerformanceReport(period: string): Observable<ReportData> {
    const params = new HttpParams().set('period', period);
    return this.http.get<ReportData>(`${this.baseUrl}/performance`, { 
      headers: this.getAuthHeaders(),
      params 
    });
  }

  /**
   * Export report in different formats
   */
  exportReport(reportData: ReportData, format: 'pdf' | 'excel' | 'csv'): Observable<Blob> {
    return this.http.post(`${this.baseUrl}/export`, {
      reportData,
      format
    }, {
      headers: this.getAuthHeaders(),
      responseType: 'blob'
    });
  }

  /**
   * Save report for later access
   */
  saveReport(reportData: ReportData): Observable<any> {
    return this.http.post(`${this.baseUrl}/save`, reportData, { 
      headers: this.getAuthHeaders() 
    });
  }

  /**
   * Get saved reports
   */
  getSavedReports(): Observable<ReportData[]> {
    return this.http.get<ReportData[]>(`${this.baseUrl}/saved`, { 
      headers: this.getAuthHeaders() 
    });
  }

  /**
   * Delete saved report
   */
  deleteReport(reportId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${reportId}`, { 
      headers: this.getAuthHeaders() 
    });
  }

  /**
   * Schedule a report
   */
  scheduleReport(schedule: ScheduledReport): Observable<ScheduledReport> {
    return this.http.post<ScheduledReport>(`${this.baseUrl}/schedule`, schedule, { 
      headers: this.getAuthHeaders() 
    });
  }

  /**
   * Get scheduled reports
   */
  getScheduledReports(): Observable<ScheduledReport[]> {
    return this.http.get<ScheduledReport[]>(`${this.baseUrl}/scheduled`, { 
      headers: this.getAuthHeaders() 
    });
  }

  /**
   * Update scheduled report
   */
  updateScheduledReport(id: string, schedule: ScheduledReport): Observable<ScheduledReport> {
    return this.http.put<ScheduledReport>(`${this.baseUrl}/scheduled/${id}`, schedule, { 
      headers: this.getAuthHeaders() 
    });
  }

  /**
   * Delete scheduled report
   */
  deleteScheduledReport(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/scheduled/${id}`, { 
      headers: this.getAuthHeaders() 
    });
  }

  /**
   * Get report templates observable
   */
  getReportTemplatesObservable(): Observable<ReportTemplate[]> {
    return this.availableReports$.asObservable();
  }

  /**
   * Get generation status
   */
  isGenerating(): Observable<boolean> {
    return this.isGenerating$.asObservable();
  }

  private loadAvailableReports(): void {
    this.getReportTemplates().subscribe();
  }
}
