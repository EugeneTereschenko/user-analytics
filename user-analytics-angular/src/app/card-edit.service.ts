import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CardEditService {

    private tokenKey = 'auth_token';
    private apiUrl = 'http://localhost:8080/api/card';

  constructor(private http: HttpClient) { }

  sendCardData(cardData: {
    cardNumber: string,
    expirationDate: string,
    cvv: string,
    cardName: string
  }): Observable<any> {
    const token = localStorage.getItem(this.tokenKey) || '';
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.post(this.apiUrl + '/send', cardData, { headers });
  }
}
