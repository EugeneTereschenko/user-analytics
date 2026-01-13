import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Prescription, PrescriptionStatus, Page } from '../models/prescription.model';

@Injectable({
  providedIn: 'root'
})
export class PrescriptionService {
  private readonly API_URL = 'http://localhost:8086/prescription-service/api/v1/prescriptions';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  createPrescription(prescription: Prescription): Observable<Prescription> {
    return this.http.post<Prescription>(this.API_URL, prescription, {
      headers: this.getHeaders()
    });
  }

  getPrescriptionById(id: number): Observable<Prescription> {
    return this.http.get<Prescription>(`${this.API_URL}/${id}`, {
      headers: this.getHeaders()
    });
  }

  getPrescriptionByNumber(prescriptionNumber: string): Observable<Prescription> {
    return this.http.get<Prescription>(`${this.API_URL}/number/${prescriptionNumber}`, {
      headers: this.getHeaders()
    });
  }

  getAllPrescriptions(page: number = 0, size: number = 10): Observable<Page<Prescription>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Page<Prescription>>(this.API_URL, {
      headers: this.getHeaders(),
      params
    });
  }

  getPrescriptionsByPatient(patientId: number, page: number = 0, size: number = 10): Observable<Page<Prescription>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Page<Prescription>>(`${this.API_URL}/patient/${patientId}`, {
      headers: this.getHeaders(),
      params
    });
  }

  getPrescriptionsByDoctor(doctorId: number, page: number = 0, size: number = 10): Observable<Page<Prescription>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Page<Prescription>>(`${this.API_URL}/doctor/${doctorId}`, {
      headers: this.getHeaders(),
      params
    });
  }

  getPrescriptionsByStatus(status: PrescriptionStatus, page: number = 0, size: number = 10): Observable<Page<Prescription>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Page<Prescription>>(`${this.API_URL}/status/${status}`, {
      headers: this.getHeaders(),
      params
    });
  }

  getRefillablePrescriptions(patientId: number): Observable<Prescription[]> {
    return this.http.get<Prescription[]>(`${this.API_URL}/patient/${patientId}/refillable`, {
      headers: this.getHeaders()
    });
  }

  getExpiredPrescriptions(): Observable<Prescription[]> {
    return this.http.get<Prescription[]>(`${this.API_URL}/expired`, {
      headers: this.getHeaders()
    });
  }

  searchPrescriptions(query: string, page: number = 0, size: number = 10): Observable<Page<Prescription>> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Page<Prescription>>(`${this.API_URL}/search`, {
      headers: this.getHeaders(),
      params
    });
  }

  updatePrescription(id: number, prescription: Prescription): Observable<Prescription> {
    return this.http.put<Prescription>(`${this.API_URL}/${id}`, prescription, {
      headers: this.getHeaders()
    });
  }

  dispensePrescription(id: number, dispensedBy: string): Observable<Prescription> {
    const params = new HttpParams().set('dispensedBy', dispensedBy);
    
    return this.http.patch<Prescription>(`${this.API_URL}/${id}/dispense`, null, {
      headers: this.getHeaders(),
      params
    });
  }

  refillPrescription(id: number): Observable<Prescription> {
    return this.http.patch<Prescription>(`${this.API_URL}/${id}/refill`, null, {
      headers: this.getHeaders()
    });
  }

  cancelPrescription(id: number, reason?: string): Observable<Prescription> {
    let params = new HttpParams();
    if (reason) {
      params = params.set('reason', reason);
    }
    
    return this.http.patch<Prescription>(`${this.API_URL}/${id}/cancel`, null, {
      headers: this.getHeaders(),
      params
    });
  }

  markExpiredPrescriptions(): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/mark-expired`, null, {
      headers: this.getHeaders()
    });
  }

  deletePrescription(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`, {
      headers: this.getHeaders()
    });
  }
}
