import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { response } from 'express';

@Injectable({ providedIn: 'root' })
export class ProfileImageService {
  private uploadUrl = 'http://localhost:8080/api/profile/upload-image';
  private getImageUrl = 'http://localhost:8080/api/profile/getImage';
  private getProfileInfoUrl = 'http://localhost:8080/api/profile/information';
  private tokenKey = 'auth_token';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  uploadProfileImage(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);

    const headers = new HttpHeaders(this.getAuthHeaders());

    return this.http.post(this.uploadUrl, formData, { headers });
  }

  loadProfileImage(): Observable<Blob> {
    const headers = new HttpHeaders(this.getAuthHeaders());

    return this.http.get(this.getImageUrl, { headers, responseType: 'blob' });
  }

  getProfileInformation(): Observable<any> {
    const headers = new HttpHeaders(this.getAuthHeaders());

    return this.http.get(this.getProfileInfoUrl, { headers });
  }
}