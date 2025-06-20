import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { response } from 'express';

@Injectable({ providedIn: 'root' })
export class ProfileImageService {
  private uploadUrl = 'http://localhost:8080/api/profile/upload-image';
  private getImageUrl = 'http://localhost:8080/api/profile/getImage';
  private getProfileInfoUrl = 'http://localhost:8080/api/profile/information';

  constructor(private http: HttpClient) {}

  uploadProfileImage(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);

    // Get token from localStorage (or wherever you store it)
    const token = localStorage.getItem('auth_token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.post(this.uploadUrl, formData, { headers }).pipe(
      response => {
        console.log('Image uploaded successfully', response);
        return response;
      }

    )
  }

  loadProfileImage(): Observable<Blob> {
    const token = localStorage.getItem('auth_token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get(this.getImageUrl, { headers, responseType: 'blob' });
  }

  getProfileInformation(): Observable<any> {
    return this.http.get(this.getProfileInfoUrl);
  }   
}