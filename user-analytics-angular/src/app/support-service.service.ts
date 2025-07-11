import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class SupportService {
  constructor(private http: HttpClient) {}

  submitFeedback(payload: { subject: string; message: string }) {
    return this.http.post('/api/support', payload);
  }
}

