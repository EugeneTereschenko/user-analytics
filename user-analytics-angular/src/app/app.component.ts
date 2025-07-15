import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'user-analytics-angular';

  showEditMenu = false;

  showProfileMenu = false;

  showBillingMenu = false;

  showEditProfileMenu = false;

  toggleEditMenu() {
    this.showEditMenu = !this.showEditMenu;
  }

  toggleProfileMenu() {
    this.showProfileMenu = !this.showProfileMenu;
  }

  toggleBillingMenu() {
    this.showBillingMenu = !this.showBillingMenu;
  }

  toggleEditProfileMenu() {
    this.showEditProfileMenu = !this.showEditProfileMenu;
  }


}
