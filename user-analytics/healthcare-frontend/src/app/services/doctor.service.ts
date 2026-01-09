import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Doctor, DoctorStatus, PageResponse } from '../models/doctor.model';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  private readonly API_URL = 'http://localhost:8085/doctor-service/api/v1/doctors';
  private http = inject(HttpClient);

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  createDoctor(doctor: Doctor): Observable<Doctor> {
    return this.http.post<Doctor>(this.API_URL, doctor, { headers: this.getHeaders() });
  }

  getDoctorById(id: number): Observable<Doctor> {
    return this.http.get<Doctor>(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
  }

  getDoctorByEmail(email: string): Observable<Doctor> {
    return this.http.get<Doctor>(`${this.API_URL}/email/${email}`, { headers: this.getHeaders() });
  }

  getDoctorByLicenseNumber(licenseNumber: string): Observable<Doctor> {
    return this.http.get<Doctor>(`${this.API_URL}/license/${licenseNumber}`, { headers: this.getHeaders() });
  }

  getAllDoctors(page: number = 0, size: number = 10): Observable<PageResponse<Doctor>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Doctor>>(this.API_URL, { params, headers: this.getHeaders() });
  }

  getDoctorsBySpecialization(specialization: string, page: number = 0, size: number = 10): Observable<PageResponse<Doctor>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Doctor>>(`${this.API_URL}/specialization/${specialization}`, { params, headers: this.getHeaders() });
  }

  getDoctorsByStatus(status: DoctorStatus, page: number = 0, size: number = 10): Observable<PageResponse<Doctor>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Doctor>>(`${this.API_URL}/status/${status}`, { params, headers: this.getHeaders() });
  }

  getDoctorsByDepartment(department: string, page: number = 0, size: number = 10): Observable<PageResponse<Doctor>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Doctor>>(`${this.API_URL}/department/${department}`, { params, headers: this.getHeaders() });
  }

  getActiveDoctors(): Observable<Doctor[]> {
    return this.http.get<Doctor[]>(`${this.API_URL}/active`, { headers: this.getHeaders() });
  }

  searchDoctors(query: string, page: number = 0, size: number = 10): Observable<PageResponse<Doctor>> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Doctor>>(`${this.API_URL}/search`, { params, headers: this.getHeaders() });
  }

  getAllSpecializations(): Observable<string[]> {
    return this.http.get<string[]>(`${this.API_URL}/specializations`, { headers: this.getHeaders() });
  }

  getAllDepartments(): Observable<string[]> {
    return this.http.get<string[]>(`${this.API_URL}/departments`, { headers: this.getHeaders() });
  }

  updateDoctor(id: number, doctor: Doctor): Observable<Doctor> {
    return this.http.put<Doctor>(`${this.API_URL}/${id}`, doctor, { headers: this.getHeaders() });
  }

  updateDoctorStatus(id: number, status: DoctorStatus): Observable<Doctor> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<Doctor>(`${this.API_URL}/${id}/status`, null, { params, headers: this.getHeaders() });
  }

  deleteDoctor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
  }
}
