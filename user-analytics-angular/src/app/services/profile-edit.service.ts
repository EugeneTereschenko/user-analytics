import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProfileEditService {

  private tokenKey = 'auth_token';
  private apiUrl = 'http://localhost:8080/api/profile'; // Adjust the base URL as needed
  
  constructor(private http: HttpClient) {}

  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }
  
  updateCertificateDates(payload: { certificateName: string, dateFrom: string; dateTo: string }): Observable<any> {
    return this.http.post(this.apiUrl + '/update-certificate-dates', payload, { headers: this.getAuthHeaders() });
  }

  getCertificateDates(): Observable<any> {
    return this.http.get(this.apiUrl + '/certificate-dates', { headers: this.getAuthHeaders() });
  }

  updateProfile(payload: any): Observable<any> {
    return this.http.post(this.apiUrl + '/update', payload, { headers: this.getAuthHeaders() });
  }

  getProfile(): Observable<any> {
    return this.http.get(this.apiUrl + '/get-profile', { headers: this.getAuthHeaders() });
  }

  updateDetails(payload: any): Observable<any> {
    return this.http.post(this.apiUrl + '/update-details', payload, { headers: this.getAuthHeaders() });
  }

  getDetails(): Observable<any> {
    console.log('Fetching profile details');
    return this.http.get(this.apiUrl + '/details', { headers: this.getAuthHeaders() });
  }

  updateEducation(payload: any): Observable<any> {
    return this.http.post(this.apiUrl + '/update-education', payload, { headers: this.getAuthHeaders() });
  }

  getEducation(): Observable<any> {
    return this.http.get(this.apiUrl + '/education', { headers: this.getAuthHeaders() });
  }

  updateExperience(payload: any): Observable<any> {
    return this.http.post(this.apiUrl + '/update-experience', payload, { headers: this.getAuthHeaders() });
  }

  getExperience(): Observable<any> {
    return this.http.get(this.apiUrl + '/experience', { headers: this.getAuthHeaders() });
  }

  updateProjects(payload: any): Observable<any> {
    return this.http.post(this.apiUrl + '/update-projects', payload, { headers: this.getAuthHeaders() });
  }

  getProjects(): Observable<any> {
    return this.http.get(this.apiUrl + '/projects', { headers: this.getAuthHeaders() });
  }

  saveSkills(payload: any): Observable<any> {
    return this.http.post(this.apiUrl + '/save-skills', payload, { headers: this.getAuthHeaders() });
  }

  getSkills(): Observable<any> {
    return this.http.get(this.apiUrl + '/skills', { headers: this.getAuthHeaders() });
  }
}