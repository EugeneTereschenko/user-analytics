// user-activity-timeline.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivityService, UserActivity, ActivityStats } from '../../services/activity.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user-activity-timeline',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-activity-timeline.component.html',
  styleUrl: './user-activity-timeline.component.css'
})
export class UserActivityTimelineComponent implements OnInit, OnDestroy {
  activities: UserActivity[] = [];
  stats: ActivityStats | null = null;
  isLoading = false;
  errorMessage: string | null = null;
  
  // Filters
  selectedType: string = 'all';
  selectedUserId: number | null = null;
  startDate: string = '';
  endDate: string = '';
  
  private subscriptions = new Subscription();

  constructor(private activityService: ActivityService) {}

  ngOnInit() {
    // Subscribe to activities
    this.subscriptions.add(
      this.activityService.getActivities().subscribe(data => {
        this.activities = data;
      })
    );

    // Subscribe to loading state
    this.subscriptions.add(
      this.activityService.isLoading().subscribe(loading => {
        this.isLoading = loading;
      })
    );

    // Subscribe to errors
    this.subscriptions.add(
      this.activityService.getError().subscribe(error => {
        this.errorMessage = error;
      })
    );

    // Load initial data
    this.loadActivities();
    this.loadStats();
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  loadActivities() {
    this.activityService.loadActivities();
  }

  loadStats() {
    this.activityService.getActivityStats().subscribe(stats => {
      this.stats = stats;
    });
  }

  filterByType() {
    if (this.selectedType === 'all') {
      this.loadActivities();
    } else {
      this.activityService.getActivitiesByType(this.selectedType).subscribe();
    }
  }

  filterByUser() {
    if (this.selectedUserId) {
      this.activityService.getActivitiesByUserId(this.selectedUserId).subscribe();
    } else {
      this.loadActivities();
    }
  }

  filterByDateRange() {
    if (this.startDate && this.endDate) {
      this.activityService.getActivitiesByDateRange(this.startDate, this.endDate).subscribe();
    } else {
      this.loadActivities();
    }
  }

  clearFilters() {
    this.selectedType = 'all';
    this.selectedUserId = null;
    this.startDate = '';
    this.endDate = '';
    this.loadActivities();
  }

  refreshData() {
    this.loadActivities();
    this.loadStats();
  }

  iconClass(type: string): string {
    switch (type) {
      case 'login': return 'bi bi-box-arrow-in-right text-primary';
      case 'logout': return 'bi bi-box-arrow-right text-secondary';
      case 'update': return 'bi bi-pencil-square text-warning';
      case 'delete': return 'bi bi-trash text-danger';
      case 'create': return 'bi bi-plus-circle text-success';
      case 'view': return 'bi bi-eye text-info';
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

  getRelativeTime(date: Date): string {
    const now = new Date();
    const diff = now.getTime() - new Date(date).getTime();
    const seconds = Math.floor(diff / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);

    if (days > 0) return `${days} day${days > 1 ? 's' : ''} ago`;
    if (hours > 0) return `${hours} hour${hours > 1 ? 's' : ''} ago`;
    if (minutes > 0) return `${minutes} minute${minutes > 1 ? 's' : ''} ago`;
    return 'Just now';
  }

  deleteActivity(activityId: number) {
    if (confirm('Are you sure you want to delete this activity?')) {
      this.activityService.deleteActivity(activityId).subscribe(() => {
        this.loadStats(); // Refresh stats after deletion
      });
    }
  }

  clearAllActivities() {
    if (confirm('Are you sure you want to clear all activities? This action cannot be undone.')) {
      this.activityService.clearAllActivities().subscribe(() => {
        this.loadStats();
      });
    }
  }

  getCountByType(type: string): number {
    return this.activityService.getCountByType(type);
  }

  getBadgeClass(type: string): string {
    switch (type) {
      case 'login': return 'badge bg-primary';
      case 'logout': return 'badge bg-secondary';
      case 'update': return 'badge bg-warning';
      case 'delete': return 'badge bg-danger';
      case 'create': return 'badge bg-success';
      case 'view': return 'badge bg-info';
      default: return 'badge bg-light text-dark';
    }
  }
}