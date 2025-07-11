import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) {}

  getUserById(id: string | number): Observable<any> {
    return this.http.get<any>(`/api/users/${id}`);
  }

  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>('/api/users');
  }

  getUser(id: string | number): Observable<any> {
    return this.http.get<any>(`/api/users/${id}`);
  }

  deactivateUser(id: string | number): Observable<any> {
    return this.http.post<any>(`/api/users/${id}/deactivate`, {});
  }

  // Add more user-related methods as needed
}
