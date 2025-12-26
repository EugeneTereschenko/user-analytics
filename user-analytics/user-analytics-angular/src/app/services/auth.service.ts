// src/app/auth.service.ts
import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'auth_token';
  private url = 'http://localhost:8080/api/auth'; // Base URL for authentication API
  private authStatus = new BehaviorSubject<boolean>(false);

  constructor(
    private http: HttpClient, 
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    // Initialize auth status only in browser
    if (isPlatformBrowser(this.platformId)) {
      this.authStatus.next(this.isAuthenticated());
    }
  }

  /**
   * Sign up a new user.
   * @param email User's email address.
   * @param password User's password.
   * @param deviceType User's device type (optional).
   * @param location User's location (optional).
   * @param roles User's roles (optional).
   * @returns An observable that emits the server response.
   */
  signUp(username: string, email: string, password: string, deviceType?: string, location?: string, roles: string[] = []) {
    const signupData: any = { username, email, password, roles };
    
    if (deviceType) {
      signupData.deviceType = deviceType;
    }
    if (location) {
      signupData.location = location;
    }
    
    return this.http.post(this.url + '/signup', signupData).pipe(
      tap(() => {
        // Optionally, you can redirect or perform other actions after successful signup
        this.router.navigate(['/signin']);
      })
    );  
  }

  /**
   * Sign in an existing user.
   * @param username User's username.
   * @param email User's email address.
   * @param password User's password.
   * @param roles Optional roles for the user.
   * @returns An observable that emits the authentication token.
   */
  signIn(username: string, email: string, password: string, roles: string[] = []) {
    return this.http.post<{ token: string }>(
      this.url + '/signin',
      { username, email, password, roles }
    ).pipe(
      tap(response => {
        console.log('Authentication token received:', response.token);
        // Store the token in localStorage
        console.log('tokenKey ', this.tokenKey);
        if (isPlatformBrowser(this.platformId)) {
          localStorage.setItem(this.tokenKey, response.token);
          this.authStatus.next(true);
        }
      })
    );
  }

  logout() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.tokenKey);
      this.authStatus.next(false);
    }
    this.router.navigate(['/signin']);
  }

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem(this.tokenKey);
    }
    return null;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  get isAuthenticated$(): Observable<boolean> {
    return this.authStatus.asObservable();
  }
} 