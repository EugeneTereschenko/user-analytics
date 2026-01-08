import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Appointment, AppointmentDTO, AppointmentStatus, PageResponse } from '../models/appointment.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  private readonly API_URL = 'http://localhost:8080/api/v1/appointments';
  private http = inject(HttpClient);

  createAppointment(appointment: AppointmentDTO): Observable<Appointment> {
    return this.http.post<Appointment>(this.API_URL, appointment);
  }

  getAppointmentById(id: number): Observable<Appointment> {
    return this.http.get<Appointment>(`${this.API_URL}/${id}`);
  }

  getAllAppointments(page: number = 0, size: number = 10): Observable<PageResponse<Appointment>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Appointment>>(this.API_URL, { params });
  }

  getAppointmentsByPatient(patientId: number, page: number = 0, size: number = 10): Observable<PageResponse<Appointment>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Appointment>>(`${this.API_URL}/patient/${patientId}`, { params });
  }

  getAppointmentsByDoctor(doctorId: number, page: number = 0, size: number = 10): Observable<PageResponse<Appointment>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Appointment>>(`${this.API_URL}/doctor/${doctorId}`, { params });
  }

  getAppointmentsByStatus(status: AppointmentStatus, page: number = 0, size: number = 10): Observable<PageResponse<Appointment>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Appointment>>(`${this.API_URL}/status/${status}`, { params });
  }

  getAppointmentsBetween(startDate: string, endDate: string): Observable<Appointment[]> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);
    return this.http.get<Appointment[]>(`${this.API_URL}/between`, { params });
  }

  getTodaysAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.API_URL}/today`);
  }

  updateAppointment(id: number, appointment: AppointmentDTO): Observable<Appointment> {
    return this.http.put<Appointment>(`${this.API_URL}/${id}`, appointment);
  }

  rescheduleAppointment(id: number, newDateTime: string): Observable<Appointment> {
    const params = new HttpParams().set('newDateTime', newDateTime);
    return this.http.patch<Appointment>(`${this.API_URL}/${id}/reschedule`, null, { params });
  }

  cancelAppointment(id: number, reason?: string): Observable<Appointment> {
    const params = reason ? new HttpParams().set('reason', reason) : new HttpParams();
    return this.http.patch<Appointment>(`${this.API_URL}/${id}/cancel`, null, { params });
  }

  confirmAppointment(id: number): Observable<Appointment> {
    return this.http.patch<Appointment>(`${this.API_URL}/${id}/confirm`, null);
  }

  completeAppointment(id: number, notes?: string): Observable<Appointment> {
    const params = notes ? new HttpParams().set('notes', notes) : new HttpParams();
    return this.http.patch<Appointment>(`${this.API_URL}/${id}/complete`, null, { params });
  }

  deleteAppointment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
