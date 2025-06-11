import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h2>Sign In</h2>
    <form (ngSubmit)="signIn()">
      <label>Email: <input [(ngModel)]="email" name="email"></label><br>
      <label>Password: <input [(ngModel)]="password" type="password" name="password"></label><br>
      <button type="submit">Sign In</button>
    </form>
  `
})
export class SigninComponent {
  email = '';
  password = '';

  signIn() {
    console.log('Signing in:', this.email, this.password);
    // Call AuthService.login() here
  }
}
