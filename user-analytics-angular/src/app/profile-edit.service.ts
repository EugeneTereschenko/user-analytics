import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProfileEditService {
  constructor(private http: HttpClient) {}

  updateCertificateDates(payload: { certificateName: string, dateFrom: string; dateTo: string }): Observable<any> {
    return this.http.post('/api/certificates/update-dates', payload);
  }

  updateProfile(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/update', payload);
  }

  updateDetails(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/update-details', payload);
  }

  updateEducation(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/update-education', payload);
  }

  updateExperience(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/update-experience', payload);
  }

  updateProjects(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/update-projects', payload);
  }

  saveSkills(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post('/api/profile/save-skills', payload);
  }
}