// activity.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface UserActivity {
  id: number;
  type: 'login' | 'update' | 'delete' | 'create';
  description: string;
  timestamp: Date;
  user?: string;
}

@Injectable({ providedIn: 'root' })
export class ActivityService {
  private activities$ = new BehaviorSubject<UserActivity[]>([]);

  logActivity(activity: UserActivity) {
    const current = this.activities$.value;
    this.activities$.next([activity, ...current]);
  }

  getActivities() {
    return this.activities$.asObservable();
  }
  
}

