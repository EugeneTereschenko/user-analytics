import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { FormsModule } from '@angular/forms';
import { CalendarService, CalendarEvent } from '../../services/calendar.service';

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [CommonModule, FullCalendarModule, FormsModule],
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CalendarComponent {
  calendarPlugins = [dayGridPlugin, interactionPlugin];
  calendarEvents = [
    { title: 'Project Deadline', date: '2025-07-20' },
    { title: 'Team Meeting', date: '2025-07-22' }
  ];
  calendarOptions: any;

  showModal = false;
  newEventTitle = '';
  newEventDate = '';



  constructor(private calendarService: CalendarService) {
    this.calendarOptions = {
      initialView: 'dayGridMonth',
      plugins: this.calendarPlugins,
      events: this.calendarEvents,
      dateClick: this.handleDateClick.bind(this)
    };
  }

  ngOnInit() {
      this.calendarService.getAllEvents().subscribe((events: CalendarEvent[]) => {
        this.calendarEvents = events;
        this.calendarOptions.events = this.calendarEvents.map(event => ({
          title: event.title,
          date: event.date
        }));
        console.log('Events loaded:', this.calendarEvents);
      }, (error) => {
        console.error('Error loading events:', error);
        this.calendarOptions.events = this.calendarEvents.map(event => ({
          title: event.title,
          date: event.date
        }));
        console.log('Using default events:', this.calendarEvents);
      });
  }

  handleDateClick(arg: any) {
    this.newEventDate = arg.dateStr;
    this.newEventTitle = '';
    this.showModal = true;
  }

  saveEvent() {
    if (this.newEventTitle.trim()) {
      this.calendarEvents = [
        ...this.calendarEvents,
        { title: this.newEventTitle, date: this.newEventDate }
      ];
      this.calendarService.saveEvent({
        title: this.newEventTitle,
        date: this.newEventDate
      }).subscribe({
        next: () => {
          console.log('Event saved successfully');
        },
        error: (err) => {
          console.error('Error saving event:', err);
        }
      });
    }
    this.showModal = false;
  }

  closeModal() {
    this.showModal = false;
  }


}
