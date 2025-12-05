import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CardEditService {
  private readonly tokenKey = 'auth_token';
  private readonly apiUrl = 'http://localhost:8080/api/card';

  constructor(private http: HttpClient) {}

  sendCardData(cardData: CardData): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/send`,
      cardData,
      { headers: this.createAuthHeaders() }
    );
  }

  private createAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem(this.tokenKey) ?? '';
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }
}

export interface CardData {
  cardNumber: string;
  expirationDate: string;
  cvv: string;
  cardName: string;
}
