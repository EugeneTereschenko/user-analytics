import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { NotificationService } from '../../services/notification.service'; // Adjust the path as necessary
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [DatePipe, CommonModule],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent implements OnInit {
  notifications: any[] = [];

  constructor(private notificationService: NotificationService) {}

  ngOnInit() {
    this.notificationService.getAll().subscribe(data => this.notifications = data);
  }

  expandedIndex: number | null = 0;

  toggleAccordion(notification: any, index: number) {
    if (this.expandedIndex === index) {
      // Closing the currently open accordion
      console.log('Closing accordion:', notification);
      this.expandedIndex = null;
      this.onAccordionHide(notification, index);
    } else {
      // Opening a new accordion
      console.log('Opening accordion:', notification);
      this.expandedIndex = index;
      this.onAccordionShow(notification, index);
    }
    console.log('Notification clicked:', notification);
    console.log('Index:', index);
  }

  onAccordionShow(notification: any, index: number) {
  console.log('Accordion opened:', notification);
  console.log('Index:', index);
  // Add your logic when message is expanded/shown
  // For example: mark as read, track view event, etc.
  }

  onAccordionHide(notification: any, index: number) {
  console.log('Accordion closed:', notification);
  console.log('Index:', index);
  // Add your logic when message is collapsed/hidden
  }
}

