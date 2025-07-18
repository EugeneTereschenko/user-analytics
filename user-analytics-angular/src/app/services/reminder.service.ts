// reminder.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

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

  getReminders() {
    return this.reminders$.asObservable();
  }

  addReminder(reminder: Reminder) {
    const current = this.reminders$.value;
    this.reminders$.next([...current, reminder]);
  }

  removeReminder(id: number) {
    const filtered = this.reminders$.value.filter(r => r.id !== id);
    this.reminders$.next(filtered);
  }
}

