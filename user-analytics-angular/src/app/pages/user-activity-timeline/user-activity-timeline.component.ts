import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivityService, UserActivity } from '../../services/activity.service';

@Component({
  selector: 'app-user-activity-timeline',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-activity-timeline.component.html',
  styleUrl: './user-activity-timeline.component.css'
})
export class UserActivityTimelineComponent implements OnInit {
  activities: UserActivity[] = [];

  constructor(private activityService: ActivityService) {}

  ngOnInit() {
    this.activityService.getActivities().subscribe(data => this.activities = data);
    this.logActivity('login', 'User logged in');
    this.logActivity('update', 'User updated profile');
    this.logActivity('delete', 'User deleted an item');
  }

  iconClass(type: string): string {
    switch (type) {
      case 'login': return 'bi bi-box-arrow-in-right text-primary';
      case 'logout': return 'bi bi-box-arrow-right text-secondary';
      case 'update': return 'bi bi-pencil-square text-warning';
      case 'delete': return 'bi bi-trash text-danger';
      case 'create': return 'bi bi-plus-circle text-success';
      default: return 'bi bi-info-circle';
    }
  }

  formatDate(date: Date): string {
    return new Intl.DateTimeFormat('en-US', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    }).format(new Date(date));
  }

  logActivity(type: 'login' | 'logout' | 'update' | 'delete' | 'create', description: string) {
    this.activityService.logActivity({
      id: Date.now(),
      type: type,
      description: description,
      timestamp: new Date(),
      user: 'admin@example.com'
    });
  }

  getCountByType(type: string): number {
    return this.activities?.filter(a => a.type === type).length || 0;
  }
}