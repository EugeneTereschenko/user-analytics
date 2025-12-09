import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AssistantResponse {
  text: string;
  type: 'success' | 'error' | 'info';
  timestamp: number;
}

@Injectable({
  providedIn: 'root'
})
export class AssistantService {

  private readonly tokenKey = 'auth_token';
  private readonly baseUrl = 'http://localhost:8080/api';
  
  constructor(private http: HttpClient) { }
  
  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  getAssistantData(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/assistant/data`, { headers: this.getAuthHeaders() });
  }
  
  sendAssistantCommand(command: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/assistant/command`, { command }, { headers: this.getAuthHeaders() });
  }
  
  getAssistantStatus(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/assistant/status`, { headers: this.getAuthHeaders() });
  }
  
  sendMessage(message: string, sessionId: string): Observable<AssistantResponse> {
    return this.http.post<AssistantResponse>(
      `${this.baseUrl}/assistant/message`,
      { text: message, sessionId },
      { headers: this.getAuthHeaders() }
    );
  }
}
