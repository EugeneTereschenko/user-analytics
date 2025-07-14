import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly tokenKey = 'auth_token';
  private readonly apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  getUserById(id: string | number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/user/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/users`, {
      headers: this.getAuthHeaders()
    });
  }

  deactivateUser(id: string | number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}${id}/deactivate`, {}, {
      headers: this.getAuthHeaders()
    });
  }
}
