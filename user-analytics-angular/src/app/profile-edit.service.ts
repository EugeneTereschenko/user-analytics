import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProfileEditService {

  private apiUrl = 'http://localhost:8080/api/profile'; // Adjust the base URL as needed
  constructor(private http: HttpClient) {}
  

  updateCertificateDates(payload: { certificateName: string, dateFrom: string; dateTo: string }): Observable<any> {
    return this.http.post(this.apiUrl + '/update-dates', payload);
  }

  getCertificateDates(): Observable<any> {
    return this.http.get(this.apiUrl + '/certificate-dates');
  }

  updateProfile(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post(this.apiUrl + '/update', payload);
  }

  getProfile(): Observable<any> {
    return this.http.get(this.apiUrl + '/get-profile');
  }

  updateDetails(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post(this.apiUrl + '/update-details', payload);
  }

  getDetails(): Observable<any> {
    console.log('Fetching profile details');
    return this.http.get(this.apiUrl + '/details');
  }

  updateEducation(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post(this.apiUrl + '/update-education', payload);
  }

  getEducation(): Observable<any> {
    return this.http.get(this.apiUrl + '/education');
  }

  updateExperience(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post(this.apiUrl + '/update-experience', payload);
  }

  getExperience(): Observable<any> {
    return this.http.get(this.apiUrl + '/experience');
  }

  updateProjects(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post(this.apiUrl + '/update-projects', payload);
  }

  getProjects(): Observable<any> {
    return this.http.get(this.apiUrl + '/projects');
  }

  saveSkills(payload: any): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.post(this.apiUrl + '/save-skills', payload);
  }

  getSkills(): Observable<any> {
    // Adjust the URL to match your backend API endpoint
    return this.http.get(this.apiUrl + '/skills');
  }
}