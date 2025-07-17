import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [CommonModule, FullCalendarModule],
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

  constructor() {
    this.calendarOptions = {
      initialView: 'dayGridMonth',
      plugins: this.calendarPlugins,
      events: this.calendarEvents,
      dateClick: this.handleDateClick.bind(this)
    };
  }

  handleDateClick(arg: any) {
    alert('Date clicked: ' + arg.dateStr);
  }
}
