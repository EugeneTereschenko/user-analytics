import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { NotificationService } from '../../notification.service'; // Adjust the path as necessary
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
}

