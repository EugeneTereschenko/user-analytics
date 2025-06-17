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
  signUp(username: string, email: string, password: string, confirmPassword: string, roles: string[] = []) {
    return this.http.post('http://localhost:8080/api/auth/signup', { username, email, password, confirmPassword, roles }).pipe(
      tap(() => {
        // Optionally, you can redirect or perform other actions after successful signup
        this.router.navigate(['/signin']);
      }
    ));  
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
    'http://localhost:8080/api/auth/signin',
    { username, email, password, roles }
  ).pipe(
    tap(response => {
      console.log('Authentication token received:', response.token);
      // Store the token in localStorage
      console.log('tokenKey ', this.tokenKey);
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

