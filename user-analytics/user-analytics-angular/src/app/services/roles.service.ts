import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class RolesService {
  private readonly tokenKey = 'auth_token';
  private readonly baseUrl = 'http://localhost:8080/api';
  
  constructor(private http: HttpClient) { }
  
  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  getUsersWithRoles() {
    return this.http.get<any[]>(`${this.baseUrl}/roles`, { headers: this.getAuthHeaders() });
  }

  updateUserRole(userId: string, role: string) {
    return this.http.put(`${this.baseUrl}/users/${userId}/role`, { role }, { headers: this.getAuthHeaders() });
  }
}

