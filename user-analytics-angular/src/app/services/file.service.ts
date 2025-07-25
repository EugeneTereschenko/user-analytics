import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileService {
  private tokenKey = 'auth_token';
  private readonly baseUrl = 'http://localhost:8080/api/files';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ message: string; status: string }>(
      `${this.baseUrl}/upload`,
      formData,
      {
        headers: this.getAuthHeaders()
      }
    );
  }

  fetchFiles(): Observable<string[]> {
    return this.http.get<string[]>(this.baseUrl, {
      headers: this.getAuthHeaders()
    });
  }

  downloadFile(fileName: string): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/download/${fileName}`, {
      headers: this.getAuthHeaders(),
      responseType: 'blob'
    });
  }

  deleteFile(fileName: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${fileName}`, {
      headers: this.getAuthHeaders()
    });
  }
}
