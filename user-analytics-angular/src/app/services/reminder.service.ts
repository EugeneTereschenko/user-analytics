// reminder.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export interface Reminder {
  id: number;
  title: string;
  date: string;
  time?: string;
  notified?: boolean;
}

@Injectable({ providedIn: 'root' })
export class ReminderService {
  private reminders$ = new BehaviorSubject<Reminder[]>([]);

  private readonly tokenKey = 'auth_token';
  private readonly baseUrl = 'http://localhost:8080/api';
  
  constructor(private http: HttpClient) { }
  
  private getAuthHeaders(): { [header: string]: string } {
    const token = localStorage.getItem(this.tokenKey);
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  getReminders() {
    if (this.reminders$.value.length === 0) {
      this.fetchReminders();
    }
    // Return an observable of the reminders
    return this.reminders$.asObservable();
  }

  addReminder(reminder: Reminder) {
    const current = this.reminders$.value;
    this.reminders$.next([...current, reminder]);
    this.saveReminder(reminder).subscribe();
  }

  removeReminder(id: number) {
    const filtered = this.reminders$.value.filter(r => r.id !== id);
    this.reminders$.next(filtered);
    this.deleteReminder(id).subscribe();
  }

  fetchReminders() {
    this.http.get<Reminder[]>(`${this.baseUrl}/reminders`, { headers: this.getAuthHeaders() })
      .subscribe(reminders => this.reminders$.next(reminders));
  }

  saveReminder(reminder: Reminder) {
    return this.http.post(`${this.baseUrl}/reminders`, reminder, {
      headers: this.getAuthHeaders()
    });
  }
  
  deleteReminder(id: number) {
    return this.http.delete(`${this.baseUrl}/reminders/${id}`, {
      headers: this.getAuthHeaders()
    });
  }
}

