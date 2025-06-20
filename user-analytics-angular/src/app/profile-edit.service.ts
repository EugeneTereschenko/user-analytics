import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProfileEditService {
  constructor(private http: HttpClient) {}

  updateCertificateDates(payload: { certificateName: string, dateFrom: string; dateTo: string }): Observable<any> {
    return this.http.post('/api/profile/update-dates', payload);
  }

  getCertificateDates(): Observable<any> {
    return this.http.get('/api/profile/certificate-dates');
  }

  updateProfile(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/update', payload);
  }

  getProfile(): Observable<any> {
    return this.http.get('/api/profile');
  }

  updateDetails(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/update-details', payload);
  }

  getDetails(): Observable<any> {
    console.log('Fetching profile details');
    return this.http.get('/api/profile/details');
  }

  updateEducation(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/update-education', payload);
  }

  getEducation(): Observable<any> {
    return this.http.get('/api/profile/education');
  }

  updateExperience(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/update-experience', payload);
  }

  getExperience(): Observable<any> {
    return this.http.get('/api/profile/experience');
  }

  updateProjects(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/update-projects', payload);
  }

  getProjects(): Observable<any> {
    return this.http.get('/api/profile/projects');
  }

  saveSkills(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/save-skills', payload);
  }

  getSkills(): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.get('/api/profile/skills');
  }
}