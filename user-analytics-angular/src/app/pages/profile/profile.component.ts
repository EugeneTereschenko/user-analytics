import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>My Profile</h2>
    <div class="card">
      <p><strong>Name:</strong> Jane Admin</p>
      <p><strong>Email:</strong> jane&#64;example.com</p>
      <p><strong>Role:</strong> Administrator</p>
    </div>
  `
})
export class ProfileComponent {}

