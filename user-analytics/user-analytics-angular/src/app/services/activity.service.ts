// activity.service.ts - Updated with HTTP integration
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, tap, of } from 'rxjs';

export interface UserActivity {
  id?: number;
  userId?: number;
  username?: string;
  type: 'login' | 'logout' | 'create' | 'update' | 'delete' | 'view';
  description: string;
  timestamp: Date;
  ipAddress?: string;
  deviceType?: string;
  location?: string;
}

export interface ActivityStats {
  totalActivities: number;
  loginCount: number;
  logoutCount: number;
  createCount: number;
  updateCount: number;
  deleteCount: number;
  viewCount: number;
}

@Injectable({ providedIn: 'root' })
export class ActivityService {
  private apiUrl = 'http://localhost:8080/api/activities'; // Adjust to your backend URL
  private activities$ = new BehaviorSubject<UserActivity[]>([]);
  private loading$ = new BehaviorSubject<boolean>(false);
  private error$ = new BehaviorSubject<string | null>(null);

  constructor(private http: HttpClient) {
    this.loadActivities();
  }

  /**
   * Get all activities from backend
   */
  loadActivities(): void {
    this.loading$.next(true);
    this.error$.next(null);

    this.http.get<UserActivity[]>(`${this.apiUrl}`)
      .pipe(
        tap(activities => {
          // Convert timestamp strings to Date objects
          const formattedActivities = activities.map(a => ({
            ...a,
            timestamp: new Date(a.timestamp)
          }));
          this.activities$.next(formattedActivities);
          this.loading$.next(false);
        }),
        catchError(error => {
          console.error('Error loading activities:', error);
          this.error$.next('Failed to load activities');
          this.loading$.next(false);
          return of([]);
        })
      )
      .subscribe();
  }

  /**
   * Get activities for a specific user
   */
  getActivitiesByUserId(userId: number): Observable<UserActivity[]> {
    return this.http.get<UserActivity[]>(`${this.apiUrl}/user/${userId}`)
      .pipe(
        tap(activities => {
          const formattedActivities = activities.map(a => ({
            ...a,
            timestamp: new Date(a.timestamp)
          }));
          this.activities$.next(formattedActivities);
        }),
        catchError(error => {
          console.error('Error loading user activities:', error);
          this.error$.next('Failed to load user activities');
          return of([]);
        })
      );
  }

  /**
   * Get activities by type
   */
  getActivitiesByType(type: string): Observable<UserActivity[]> {
    return this.http.get<UserActivity[]>(`${this.apiUrl}/type/${type}`)
      .pipe(
        catchError(error => {
          console.error('Error loading activities by type:', error);
          return of([]);
        })
      );
  }

  /**
   * Get activities within a date range
   */
  getActivitiesByDateRange(startDate: string, endDate: string): Observable<UserActivity[]> {
    return this.http.get<UserActivity[]>(`${this.apiUrl}/date-range`, {
      params: { startDate, endDate }
    }).pipe(
      tap(activities => {
        const formattedActivities = activities.map(a => ({
          ...a,
          timestamp: new Date(a.timestamp)
        }));
        this.activities$.next(formattedActivities);
      }),
      catchError(error => {
        console.error('Error loading activities by date range:', error);
        return of([]);
      })
    );
  }

  /**
   * Log a new activity
   */
  logActivity(activity: Omit<UserActivity, 'id' | 'timestamp'>): Observable<UserActivity> {
    return this.http.post<UserActivity>(`${this.apiUrl}`, activity)
      .pipe(
        tap(newActivity => {
          const current = this.activities$.value;
          this.activities$.next([
            { ...newActivity, timestamp: new Date(newActivity.timestamp) },
            ...current
          ]);
        }),
        catchError(error => {
          console.error('Error logging activity:', error);
          this.error$.next('Failed to log activity');
          return of(activity as UserActivity);
        })
      );
  }

  /**
   * Get activity statistics
   */
  getActivityStats(): Observable<ActivityStats> {
    return this.http.get<ActivityStats>(`${this.apiUrl}/stats`)
      .pipe(
        catchError(error => {
          console.error('Error loading activity stats:', error);
          return of({
            totalActivities: 0,
            loginCount: 0,
            logoutCount: 0,
            createCount: 0,
            updateCount: 0,
            deleteCount: 0,
            viewCount: 0
          });
        })
      );
  }

  /**
   * Get recent activities (last N activities)
   */
  getRecentActivities(limit: number = 10): Observable<UserActivity[]> {
    return this.http.get<UserActivity[]>(`${this.apiUrl}/recent`, {
      params: { limit: limit.toString() }
    }).pipe(
      tap(activities => {
        const formattedActivities = activities.map(a => ({
          ...a,
          timestamp: new Date(a.timestamp)
        }));
        this.activities$.next(formattedActivities);
      }),
      catchError(error => {
        console.error('Error loading recent activities:', error);
        return of([]);
      })
    );
  }

  /**
   * Delete an activity (admin only)
   */
  deleteActivity(activityId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${activityId}`)
      .pipe(
        tap(() => {
          const current = this.activities$.value;
          this.activities$.next(current.filter(a => a.id !== activityId));
        }),
        catchError(error => {
          console.error('Error deleting activity:', error);
          this.error$.next('Failed to delete activity');
          return of(void 0);
        })
      );
  }

  /**
   * Clear all activities (admin only)
   */
  clearAllActivities(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/clear`)
      .pipe(
        tap(() => {
          this.activities$.next([]);
        }),
        catchError(error => {
          console.error('Error clearing activities:', error);
          this.error$.next('Failed to clear activities');
          return of(void 0);
        })
      );
  }

  // Observable getters
  getActivities(): Observable<UserActivity[]> {
    return this.activities$.asObservable();
  }

  isLoading(): Observable<boolean> {
    return this.loading$.asObservable();
  }

  getError(): Observable<string | null> {
    return this.error$.asObservable();
  }

  // Helper method to get count by type from current activities
  getCountByType(type: string): number {
    return this.activities$.value.filter(a => a.type === type).length;
  }
}