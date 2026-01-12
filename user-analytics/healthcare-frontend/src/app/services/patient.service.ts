import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
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
  private apiUrl = '/api/v1/patients';

  constructor(private http: HttpClient) { }

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

    return this.http.get<PageResponse<Patient>>(this.apiUrl, { params });
  }

  getPatientById(id: number): Observable<Patient> {
    return this.http.get<Patient>(`${this.apiUrl}/${id}`);
  }

  createPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(this.apiUrl, patient);
  }

  updatePatient(id: number, patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(`${this.apiUrl}/${id}`, patient);
  }

  deletePatient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  deactivatePatient(id: number): Observable<Patient> {
    return this.http.patch<Patient>(`${this.apiUrl}/${id}/deactivate`, {});
  }
}
