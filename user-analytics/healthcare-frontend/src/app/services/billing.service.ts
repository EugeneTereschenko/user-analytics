import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Invoice, Payment, InvoiceStatus, Page } from '../models/billing.model';

@Injectable({
  providedIn: 'root'
})
export class BillingService {
  private readonly API_URL = 'http://localhost:8082/billing-service/api/v1/billing';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  createInvoice(invoice: Invoice): Observable<Invoice> {
    return this.http.post<Invoice>(`${this.API_URL}/invoices`, invoice, {
      headers: this.getHeaders()
    });
  }

  getInvoiceById(id: number): Observable<Invoice> {
    return this.http.get<Invoice>(`${this.API_URL}/invoices/${id}`, {
      headers: this.getHeaders()
    });
  }

  getInvoiceByNumber(invoiceNumber: string): Observable<Invoice> {
    return this.http.get<Invoice>(`${this.API_URL}/invoices/number/${invoiceNumber}`, {
      headers: this.getHeaders()
    });
  }

  getAllInvoices(page: number = 0, size: number = 10): Observable<Page<Invoice>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Page<Invoice>>(`${this.API_URL}/invoices`, {
      headers: this.getHeaders(),
      params
    });
  }

  getInvoicesByPatient(patientId: number, page: number = 0, size: number = 10): Observable<Page<Invoice>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Page<Invoice>>(`${this.API_URL}/invoices/patient/${patientId}`, {
      headers: this.getHeaders(),
      params
    });
  }

  getInvoicesByStatus(status: InvoiceStatus, page: number = 0, size: number = 10): Observable<Page<Invoice>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Page<Invoice>>(`${this.API_URL}/invoices/status/${status}`, {
      headers: this.getHeaders(),
      params
    });
  }

  getOverdueInvoices(): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(`${this.API_URL}/invoices/overdue`, {
      headers: this.getHeaders()
    });
  }

  getPatientOutstandingInvoices(patientId: number): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(`${this.API_URL}/invoices/patient/${patientId}/outstanding`, {
      headers: this.getHeaders()
    });
  }

  getTotalOutstanding(): Observable<number> {
    return this.http.get<number>(`${this.API_URL}/invoices/outstanding/total`, {
      headers: this.getHeaders()
    });
  }

  searchInvoices(query: string, page: number = 0, size: number = 10): Observable<Page<Invoice>> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Page<Invoice>>(`${this.API_URL}/invoices/search`, {
      headers: this.getHeaders(),
      params
    });
  }

  updateInvoice(id: number, invoice: Invoice): Observable<Invoice> {
    return this.http.put<Invoice>(`${this.API_URL}/invoices/${id}`, invoice, {
      headers: this.getHeaders()
    });
  }

  sendInvoice(id: number): Observable<Invoice> {
    return this.http.patch<Invoice>(`${this.API_URL}/invoices/${id}/send`, null, {
      headers: this.getHeaders()
    });
  }

  addPayment(invoiceId: number, payment: Payment): Observable<Payment> {
    return this.http.post<Payment>(`${this.API_URL}/invoices/${invoiceId}/payments`, payment, {
      headers: this.getHeaders()
    });
  }

  cancelInvoice(id: number): Observable<Invoice> {
    return this.http.patch<Invoice>(`${this.API_URL}/invoices/${id}/cancel`, null, {
      headers: this.getHeaders()
    });
  }

  deleteInvoice(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/invoices/${id}`, {
      headers: this.getHeaders()
    });
  }
}
