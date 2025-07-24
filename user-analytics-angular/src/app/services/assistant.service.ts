import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface Message {
  sender: string;
  text: string;
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

  getAssistantData() {
    return this.http.get<any>(`${this.baseUrl}/assistant/data`, { headers: this.getAuthHeaders() });
  }
  sendAssistantCommand(command: string) {
    return this.http.post(`${this.baseUrl}/assistant/command`, { command }, { headers: this.getAuthHeaders() });
  }
  getAssistantStatus() {
    return this.http.get<any>(`${this.baseUrl}/assistant/status`, { headers: this.getAuthHeaders() });
  }
  sendMessage(message: string) {
    return this.http.post(`${this.baseUrl}/assistant/message`, { text: message }, { headers: this.getAuthHeaders() });
  }
}
