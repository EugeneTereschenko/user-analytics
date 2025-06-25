import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProfileEditService {

  private tokenKey = 'auth_token';
  private apiUrl = 'http://localhost:8080/api/profile'; // Adjust the base URL as needed
  constructor(private http: HttpClient) {}
  

  updateCertificateDates(payload: { certificateName: string, dateFrom: string; dateTo: string }): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.post(this.apiUrl + '/update-certificate-dates', payload, { headers });
  }

  getCertificateDates(): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.get(this.apiUrl + '/certificate-dates', { headers });
  }

  updateProfile(payload: any): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.post(this.apiUrl + '/update', payload, { headers });
  }

  getProfile(): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.get(this.apiUrl + '/get-profile', { headers });
  }

  updateDetails(payload: any): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.post(this.apiUrl + '/update-details', payload, { headers });
  }

  getDetails(): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    console.log('Fetching profile details');
    return this.http.get(this.apiUrl + '/details', { headers });
  }

  updateEducation(payload: any): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.post(this.apiUrl + '/update-education', payload, { headers });
  }

  getEducation(): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.get(this.apiUrl + '/education', { headers });
  }

  updateExperience(payload: any): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.post(this.apiUrl + '/update-experience', payload, { headers });
  }

  getExperience(): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.get(this.apiUrl + '/experience', { headers });
  }

  updateProjects(payload: any): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.post(this.apiUrl + '/update-projects', payload, { headers });
  }

  getProjects(): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.get(this.apiUrl + '/projects', { headers });
  }

  saveSkills(payload: any): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.post(this.apiUrl + '/save-skills', payload, { headers });
  }

  getSkills(): Observable<any> {
    const token = localStorage.getItem(this.tokenKey);
    let headers = {};
    if (token) {
      headers = { 'Authorization': `Bearer ${token}` };
    }
    return this.http.get(this.apiUrl + '/skills', { headers });
  }
}