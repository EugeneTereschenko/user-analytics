import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, of, interval } from 'rxjs';
import { switchMap } from 'rxjs/operators';

export interface NotificationDTO {
  id?: number;
  title: string;
  message: string;
  timestamp: string;
  type?: 'INFO' | 'SUCCESS' | 'WARNING' | 'ERROR' | 'MESSAGE';
  priority?: 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
  isRead?: boolean;
  readAt?: string;
  category?: string;
  actionUrl?: string;
  sender?: string;
  metadata?: any;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly tokenKey = 'auth_token';
  private readonly apiUrl = 'http://localhost:8080/api/notifications';

  private notifications$ = new BehaviorSubject<NotificationDTO[]>([]);
  private unreadCount$ = new BehaviorSubject<number>(0);
  private pollingInterval = 30000; // 30 seconds

  constructor(private http: HttpClient) {
    this.loadNotifications();
    this.startPolling();
  }



  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

    /**
   * Get all notifications
   */
  getAll(): Observable<NotificationDTO[]> {
    return this.http.get<NotificationDTO[]>(this.apiUrl, { headers: this.getAuthHeaders() }).pipe(
      tap(notifications => {
        this.notifications$.next(notifications);
        this.updateUnreadCount(notifications);
      }),
      catchError(error => {
        console.error('Error fetching notifications:', error);
        return of([]);
      })
    );
  }



  /**
   * Get unread notifications
   */
  getUnread(): Observable<NotificationDTO[]> {
    return this.http.get<NotificationDTO[]>(`${this.apiUrl}/unread`, { headers: this.getAuthHeaders() });
  }

  /**
   * Get notifications by type
   */
  getByType(type: string): Observable<NotificationDTO[]> {
    return this.http.get<NotificationDTO[]>(`${this.apiUrl}/type/${type}`, { headers: this.getAuthHeaders() });
  }

  /**
   * Get notifications by priority
   */
  getByPriority(priority: string): Observable<NotificationDTO[]> {
    return this.http.get<NotificationDTO[]>(`${this.apiUrl}/priority/${priority}`, { headers: this.getAuthHeaders() });
  }

  /**
   * Mark notification as read
   */
  markAsRead(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/read`, {}, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.loadNotifications())
    );
  }

  /**
   * Mark all as read
   */
  markAllAsRead(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/read-all`, {}, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.loadNotifications())
    );
  }

  /**
   * Delete notification
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.loadNotifications())
    );
  }

  /**
   * Delete all read notifications
   */
  deleteAllRead(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/read`, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.loadNotifications())
    );
  }

  /**
   * Create notification (admin)
   */
  create(notification: NotificationDTO): Observable<NotificationDTO> {
    return this.http.post<NotificationDTO>(this.apiUrl, notification, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.loadNotifications())
    );
  }

  /**
   * Get notifications observable
   */
  getNotifications(): Observable<NotificationDTO[]> {
    return this.notifications$.asObservable();
  }

  /**
   * Get unread count observable
   */
  getUnreadCount(): Observable<number> {
    return this.unreadCount$.asObservable();
  }

  /**
   * Search notifications
   */
  search(query: string): Observable<NotificationDTO[]> {
    return this.http.get<NotificationDTO[]>(`${this.apiUrl}/search`, {
      params: { q: query },
      headers: this.getAuthHeaders()
    });
  }

  /**
   * Load notifications
   */
  private loadNotifications(): void {
    this.getAll().subscribe();
  }

  /**
   * Update unread count
   */
  private updateUnreadCount(notifications: NotificationDTO[]): void {
    const unreadCount = notifications.filter(n => !n.isRead).length;
    this.unreadCount$.next(unreadCount);
  }

  /**
   * Start polling for new notifications
   */
  private startPolling(): void {
    interval(this.pollingInterval).pipe(
      switchMap(() => this.getAll())
    ).subscribe();
  }

  /**
   * Request browser notification permission
   */
  async requestPermission(): Promise<boolean> {
    if ('Notification' in window) {
      const permission = await Notification.requestPermission();
      return permission === 'granted';
    }
    return false;
  }

  /**
   * Show browser notification
   */
  showBrowserNotification(notification: NotificationDTO): void {
    if ('Notification' in window && Notification.permission === 'granted') {
      new Notification(notification.title, {
        body: notification.message,
        icon: '/assets/notification-icon.png',
        badge: '/assets/badge-icon.png',
        tag: notification.id?.toString()
      });
    }
  }
}
