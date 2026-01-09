import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Staff, StaffRole, StaffStatus, PageResponse } from '../models/staff.model';

@Injectable({
  providedIn: 'root'
})
export class StaffService {
  private readonly API_URL = 'http://localhost:8085/doctor-service/api/v1/staff';
  private http = inject(HttpClient);

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  createStaff(staff: Staff): Observable<Staff> {
    return this.http.post<Staff>(this.API_URL, staff, { headers: this.getHeaders() });
  }

  getStaffById(id: number): Observable<Staff> {
    return this.http.get<Staff>(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
  }

  getStaffByEmail(email: string): Observable<Staff> {
    return this.http.get<Staff>(`${this.API_URL}/email/${email}`, { headers: this.getHeaders() });
  }

  getAllStaff(page: number = 0, size: number = 10): Observable<PageResponse<Staff>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Staff>>(this.API_URL, { params, headers: this.getHeaders() });
  }

  getStaffByRole(role: StaffRole, page: number = 0, size: number = 10): Observable<PageResponse<Staff>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Staff>>(`${this.API_URL}/role/${role}`, { params, headers: this.getHeaders() });
  }

  getStaffByStatus(status: StaffStatus, page: number = 0, size: number = 10): Observable<PageResponse<Staff>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Staff>>(`${this.API_URL}/status/${status}`, { params, headers: this.getHeaders() });
  }

  getStaffByDepartment(department: string, page: number = 0, size: number = 10): Observable<PageResponse<Staff>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Staff>>(`${this.API_URL}/department/${department}`, { params, headers: this.getHeaders() });
  }

  getActiveStaff(): Observable<Staff[]> {
    return this.http.get<Staff[]>(`${this.API_URL}/active`, { headers: this.getHeaders() });
  }

  searchStaff(query: string, page: number = 0, size: number = 10): Observable<PageResponse<Staff>> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Staff>>(`${this.API_URL}/search`, { params, headers: this.getHeaders() });
  }

  updateStaff(id: number, staff: Staff): Observable<Staff> {
    return this.http.put<Staff>(`${this.API_URL}/${id}`, staff, { headers: this.getHeaders() });
  }

  updateStaffStatus(id: number, status: StaffStatus): Observable<Staff> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<Staff>(`${this.API_URL}/${id}/status`, null, { params, headers: this.getHeaders() });
  }

  deleteStaff(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
  }
}
