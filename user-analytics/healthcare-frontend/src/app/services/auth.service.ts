import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { Router } from '@angular/router';
import { 
  User, 
  AuthRequest, 
  AuthResponse, 
  RegisterRequest, 
  TokenValidationResponse, 
  ApiResponse, 
  PasswordResetConfirmRequest, 
  ChangePasswordRequest 
} from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8087/api/auth';
  private http = inject(HttpClient);
  private router = inject(Router);
  private platformId = inject(PLATFORM_ID);
  private isBrowser: boolean;
  
  private currentUserSubject = new BehaviorSubject<User | null>(this.getCurrentUser());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  login(credentials: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials).pipe(
      tap(response => {
        this.setSession(response);
        this.currentUserSubject.next(response.user);
      })
    );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, request).pipe(
      tap(response => {
        this.setSession(response);
        this.currentUserSubject.next(response.user);
      })
    );
  }

  validateToken(token: string): Observable<TokenValidationResponse> {
    return this.http.post<TokenValidationResponse>(`${this.API_URL}/validate`, { token });
  }

  requestPasswordReset(email: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.API_URL}/password-reset/request`, { email });
  }

  confirmPasswordReset(request: PasswordResetConfirmRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.API_URL}/password-reset/confirm`, request);
  }

  changePassword(request: ChangePasswordRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.API_URL}/password/change`, request);
  }

  verifyEmail(token: string): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.API_URL}/verify-email?token=${token}`);
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
    }
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token;
  }

  getToken(): string | null {
    if (!this.isBrowser) return null;
    return localStorage.getItem('token');
  }

  getRefreshToken(): string | null {
    if (!this.isBrowser) return null;
    return localStorage.getItem('refreshToken');
  }

  getCurrentUser(): User | null {
    if (!this.isBrowser) return null;
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  }

  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return user?.roles?.includes(role) || false;
  }

  hasPermission(permission: string): boolean {
    const user = this.getCurrentUser();
    return user?.permissions?.includes(permission) || false;
  }

  private setSession(authResult: AuthResponse): void {
    if (this.isBrowser) {
      localStorage.setItem('token', authResult.token);
      localStorage.setItem('refreshToken', authResult.refreshToken);
      localStorage.setItem('user', JSON.stringify(authResult.user));
    }
  }
}
