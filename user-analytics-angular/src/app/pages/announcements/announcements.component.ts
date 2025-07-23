import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnnouncementsService } from '../../services/announcements.service';

@Component({
  selector: 'app-announcements',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './announcements.component.html',
  styleUrl: './announcements.component.css'
})
export class AnnouncementsComponent {
  announcements: any[] = [];

  constructor(private announcementsService: AnnouncementsService) { }

  ngOnInit(): void {
    this.announcementsService.getAllAnnouncements().subscribe(data => {
      this.announcements = data;
    }, error => {
      console.error('Error fetching announcements:', error);
    });
  }

}
