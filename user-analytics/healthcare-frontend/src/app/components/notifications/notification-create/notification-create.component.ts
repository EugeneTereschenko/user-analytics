import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NotificationService } from '../../../services/notification.service';
import { Notification, NotificationType, NotificationChannel } from '../../../models/notification.model';

@Component({
  selector: 'app-notification-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './notification-create.component.html',
  styleUrls: ['./notification-create.component.css']
})
export class NotificationCreateComponent {
  notification: Notification = {
    recipientId: 1,
    notificationType: 'APPOINTMENT_REMINDER',
    channel: 'EMAIL',
    message: '',
    subject: ''
  };

  notificationTypes = Object.values(NotificationType);
  channels = Object.values(NotificationChannel);
  
  loading = false;
  errorMessage: string | null = null;

  constructor(
    private notificationService: NotificationService,
    private router: Router
  ) {}

  createNotification() {
    this.loading = true;
    this.errorMessage = null;

    this.notificationService.createNotification(this.notification)
      .subscribe({
        next: () => {
          alert('Notification created successfully!');
          this.router.navigate(['/notifications']);
        },
        error: (err) => {
          this.errorMessage = 'Failed to create notification: ' + err.message;
          this.loading = false;
          console.error('Error creating notification:', err);
        }
      });
  }

  cancel() {
    this.router.navigate(['/notifications']);
  }

  formatEnumValue(value: string): string {
    return value.replace(/_/g, ' ');
  }
}
