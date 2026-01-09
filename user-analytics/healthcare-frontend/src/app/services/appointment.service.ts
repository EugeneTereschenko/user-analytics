import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Appointment, AppointmentDTO, AppointmentStatus, PageResponse } from '../models/appointment.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  private readonly API_URL = 'http://localhost:8083/appointment-service/api/v1/appointments';
  private http = inject(HttpClient);

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  createAppointment(appointment: AppointmentDTO): Observable<Appointment> {
    return this.http.post<Appointment>(this.API_URL, appointment, { headers: this.getHeaders() });
  }

  getAppointmentById(id: number): Observable<Appointment> {
    return this.http.get<Appointment>(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
  }

  getAllAppointments(page: number = 0, size: number = 10): Observable<PageResponse<Appointment>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Appointment>>(this.API_URL, { params, headers: this.getHeaders() });
  }

  getAppointmentsByPatient(patientId: number, page: number = 0, size: number = 10): Observable<PageResponse<Appointment>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Appointment>>(`${this.API_URL}/patient/${patientId}`, { params, headers: this.getHeaders() });
  }

  getAppointmentsByDoctor(doctorId: number, page: number = 0, size: number = 10): Observable<PageResponse<Appointment>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Appointment>>(`${this.API_URL}/doctor/${doctorId}`, { params, headers: this.getHeaders() });
  }

  getAppointmentsByStatus(status: AppointmentStatus, page: number = 0, size: number = 10): Observable<PageResponse<Appointment>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Appointment>>(`${this.API_URL}/status/${status}`, { params, headers: this.getHeaders() });
  }

  getAppointmentsBetween(startDate: string, endDate: string): Observable<Appointment[]> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);
    return this.http.get<Appointment[]>(`${this.API_URL}/between`, { params, headers: this.getHeaders() });
  }

  getTodaysAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.API_URL}/today`, { headers: this.getHeaders() });
  }

  updateAppointment(id: number, appointment: AppointmentDTO): Observable<Appointment> {
    return this.http.put<Appointment>(`${this.API_URL}/${id}`, appointment, { headers: this.getHeaders() });
  }

  rescheduleAppointment(id: number, newDateTime: string): Observable<Appointment> {
    const params = new HttpParams().set('newDateTime', newDateTime);
    return this.http.patch<Appointment>(`${this.API_URL}/${id}/reschedule`, null, { params, headers: this.getHeaders() });
  }

  cancelAppointment(id: number, reason?: string): Observable<Appointment> {
    const params = reason ? new HttpParams().set('reason', reason) : new HttpParams();
    return this.http.patch<Appointment>(`${this.API_URL}/${id}/cancel`, null, { params, headers: this.getHeaders() });
  }

  confirmAppointment(id: number): Observable<Appointment> {
    return this.http.patch<Appointment>(`${this.API_URL}/${id}/confirm`, null, { headers: this.getHeaders() });
  }

  completeAppointment(id: number, notes?: string): Observable<Appointment> {
    const params = notes ? new HttpParams().set('notes', notes) : new HttpParams();
    return this.http.patch<Appointment>(`${this.API_URL}/${id}/complete`, null, { params, headers: this.getHeaders() });
  }

  deleteAppointment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
  }
}
