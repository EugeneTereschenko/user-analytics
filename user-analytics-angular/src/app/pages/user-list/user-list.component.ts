import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>User List</h2>
    <ul>
      <li *ngFor="let user of users">{{ user }}</li>
    </ul>
  `
})
export class UserListComponent {
  users = ['Alice', 'Bob', 'Charlie'];
}



