import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject, tap, catchError, of } from 'rxjs';


export interface AnnouncementDTO {
  id?: number;
  title: string;
  body: string;
  date: string;
  author?: string;
  priority?: 'low' | 'medium' | 'high' | 'urgent';
  category?: 'general' | 'maintenance' | 'feature' | 'security' | 'event';
  isRead?: boolean;
  expiryDate?: string;
  attachments?: string[];
}

@Injectable({
  providedIn: 'root'
})
export class AnnouncementsService {
  private tokenKey = 'auth_token';
  private apiUrl = 'http://localhost:8080/api/announcements';
  private announcements$ = new BehaviorSubject<AnnouncementDTO[]>([]);
  private unreadCount$ = new BehaviorSubject<number>(0);

  constructor(private http: HttpClient) {
    this.loadAnnouncements();
  }

  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  /**
   * Get all announcements
   */
  getAllAnnouncements(): Observable<AnnouncementDTO[]> {
    return this.http.get<AnnouncementDTO[]>(this.apiUrl, { headers: this.getAuthHeaders() }).pipe(
      tap(announcements => {
        this.announcements$.next(announcements);
        this.updateUnreadCount(announcements);
      }),
      catchError(error => {
        console.error('Error fetching announcements:', error);
        return of([]);
      })
    );
  }


  /**
   * Get announcements by priority
   */
  getAnnouncementsByPriority(priority: string): Observable<AnnouncementDTO[]> {
    return this.http.get<AnnouncementDTO[]>(`${this.apiUrl}/priority/${priority}`, { headers: this.getAuthHeaders() });
  }

  /**
   * Get announcements by category
   */
  getAnnouncementsByCategory(category: string): Observable<AnnouncementDTO[]> {
    return this.http.get<AnnouncementDTO[]>(`${this.apiUrl}/category/${category}`, { headers: this.getAuthHeaders() });
  }

  /**
   * Get unread announcements
   */
  getUnreadAnnouncements(): Observable<AnnouncementDTO[]> {
    return this.http.get<AnnouncementDTO[]>(`${this.apiUrl}/unread`, { headers: this.getAuthHeaders() });
  }

  /**
   * Get active announcements (not expired)
   */
  getActiveAnnouncements(): Observable<AnnouncementDTO[]> {
    return this.http.get<AnnouncementDTO[]>(`${this.apiUrl}/active`, { headers: this.getAuthHeaders() });
  }

  /**
   * Get single announcement by ID
   */
  getAnnouncementById(id: number): Observable<AnnouncementDTO> {
    return this.http.get<AnnouncementDTO>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  /**
   * Create new announcement
   */
  createAnnouncement(announcement: AnnouncementDTO): Observable<AnnouncementDTO> {
    return this.http.post<AnnouncementDTO>(this.apiUrl, announcement, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.loadAnnouncements())
    );
  }

  /**
   * Update announcement
   */
  updateAnnouncement(id: number, announcement: AnnouncementDTO): Observable<AnnouncementDTO> {
    return this.http.put<AnnouncementDTO>(`${this.apiUrl}/${id}`, announcement, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.loadAnnouncements())
    );
  }

  /**
   * Delete announcement
   */
  deleteAnnouncement(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.loadAnnouncements())
    );
  }

  /**
   * Mark announcement as read
   */
  markAsRead(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/read`, {}, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.loadAnnouncements())
    );
  }

  /**
   * Mark all as read
   */
  markAllAsRead(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/read-all`, {}, { headers: this.getAuthHeaders() }).pipe(
      tap(() => this.loadAnnouncements())
    );
  }

  /**
   * Get announcements observable
   */
  getAnnouncementsObservable(): Observable<AnnouncementDTO[]> {
    return this.announcements$.asObservable();
  }

  /**
   * Get unread count observable
   */
  getUnreadCount(): Observable<number> {
    return this.unreadCount$.asObservable();
  }

  /**
   * Load announcements
   */
  private loadAnnouncements(): void {
    this.getAllAnnouncements().subscribe();
  }

  /**
   * Update unread count
   */
  private updateUnreadCount(announcements: AnnouncementDTO[]): void {
    const unreadCount = announcements.filter(a => !a.isRead).length;
    this.unreadCount$.next(unreadCount);
  }

  /**
   * Search announcements
   */
  searchAnnouncements(query: string): Observable<AnnouncementDTO[]> {
    return this.http.get<AnnouncementDTO[]>(`${this.apiUrl}/search`, {
      params: { q: query },
      headers: this.getAuthHeaders()
    });
  }
}
