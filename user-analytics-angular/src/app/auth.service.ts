// src/app/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'auth_token';

  constructor(private http: HttpClient, private router: Router) {}
  /**
   * Sign up a new user.
   * @param email User's email address.
   * @param password User's password.
   * @param confirmPassword Confirmation of the user's password.
   * @returns An observable that emits the server response.
   */
  signUp(email: string, password: string, confirmPassword: string) {
    return this.http.post('http://localhost:8080/api/auth/signup', { email, password, confirmPassword });
  }

  /**
   * Sign in an existing user.
   * @param email User's email address.
   * @param password User's password.
   * @returns An observable that emits the authentication token.
   */
  signIn(email: string, password: string) {
    return this.http.post<{ token: string }>('http://localhost:8080/api/auth/signin', { email, password }).pipe(
      tap(response => {
        localStorage.setItem(this.tokenKey, response.token);
      })
    );
  }


  logout() {
    localStorage.removeItem(this.tokenKey);
    this.router.navigate(['/signin']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}

