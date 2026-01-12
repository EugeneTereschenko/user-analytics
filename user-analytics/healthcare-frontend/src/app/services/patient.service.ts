import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Patient, PatientStatus } from '../models/patient.model';

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private apiUrl = 'http://localhost:8081/patient-service/api/v1/patients';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getPatients(page: number = 0, size: number = 10, search?: string, status?: PatientStatus): Observable<PageResponse<Patient>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search) {
      params = params.set('search', search);
    }

    if (status && status !== 'ALL' as any) {
      params = params.set('status', status);
    }

    return this.http.get<PageResponse<Patient>>(this.apiUrl, { params, headers: this.getHeaders() });
  }

  getPatientById(id: number): Observable<Patient> {
    return this.http.get<Patient>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  createPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(this.apiUrl, patient, { headers: this.getHeaders() });
  }

  updatePatient(id: number, patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(`${this.apiUrl}/${id}`, patient, { headers: this.getHeaders() });
  }

  deletePatient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  deactivatePatient(id: number): Observable<Patient> {
    return this.http.patch<Patient>(`${this.apiUrl}/${id}/deactivate`, {}, { headers: this.getHeaders() });
  }
}
