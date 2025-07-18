import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReminderService, Reminder } from '../../services/reminder.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-reminders',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reminders.component.html',
  styleUrl: './reminders.component.css'
})
export class RemindersComponent implements OnInit {
  newReminder = '';
  date = '';
  reminders: Reminder[] = [];

  constructor(private reminderService: ReminderService) {}

  ngOnInit() {
    this.reminderService.getReminders().subscribe(reminders => this.reminders = reminders);
  }

  addReminder() {
    const reminder: Reminder = {
      id: Date.now(),
      title: this.newReminder,
      date: this.date
    };
    this.reminderService.addReminder(reminder);
    this.newReminder = '';
    this.date = '';
  }

  deleteReminder(id: number) {
    this.reminderService.removeReminder(id);
  }
}
