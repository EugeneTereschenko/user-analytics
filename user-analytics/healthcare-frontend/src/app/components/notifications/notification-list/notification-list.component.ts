import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../../services/notification.service';
import { Notification } from '../../../models/notification.model';

@Component({
  selector: 'app-notification-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.css']
})
export class NotificationListComponent implements OnInit {
  notifications: Notification[] = [];
  recipientId: number = 1; // Replace with actual logged-in user ID
  loading = false;
  errorMessage: string | null = null;

  constructor(private notificationService: NotificationService) { }

  ngOnInit() {
    this.fetchNotifications();
  }

  fetchNotifications() {
    this.loading = true;
    this.errorMessage = null;
    this.notificationService.getNotificationsByRecipient(this.recipientId)
      .subscribe({
        next: (notifications) => {
          this.notifications = notifications;
          this.loading = false;
        },
        error: (err) => {
          this.errorMessage = 'Failed to load notifications';
          this.loading = false;
          console.error('Error fetching notifications:', err);
        }
      });
  }

  cancelNotification(id: number) {
    if (confirm('Are you sure you want to cancel this notification?')) {
      this.notificationService.cancelNotification(id)
        .subscribe({
          next: () => this.fetchNotifications(),
          error: (err) => {
            alert('Failed to cancel notification');
            console.error('Error canceling notification:', err);
          }
        });
    }
  }

  processScheduled() {
    this.notificationService.processScheduled()
      .subscribe({
        next: () => {
          alert('Scheduled notifications processed successfully');
          this.fetchNotifications();
        },
        error: (err) => {
          alert('Failed to process scheduled notifications');
          console.error('Error processing scheduled notifications:', err);
        }
      });
  }

  retryFailed() {
    this.notificationService.retryFailed()
      .subscribe({
        next: () => {
          alert('Failed notifications retried successfully');
          this.fetchNotifications();
        },
        error: (err) => {
          alert('Failed to retry notifications');
          console.error('Error retrying failed notifications:', err);
        }
      });
  }

  getStatusClass(status?: string): string {
    switch (status) {
      case 'SENT':
        return 'badge bg-success';
      case 'PENDING':
        return 'badge bg-warning';
      case 'FAILED':
        return 'badge bg-danger';
      case 'CANCELLED':
        return 'badge bg-secondary';
      default:
        return 'badge bg-info';
    }
  }
}
