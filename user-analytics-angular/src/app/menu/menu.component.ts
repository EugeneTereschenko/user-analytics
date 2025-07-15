import { Component } from '@angular/core';
import { FeatureToggleService } from '../feature-toggle.service'; // Import the feature toggle service
import { RouterOutlet } from '@angular/router';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [RouterOutlet, RouterModule, CommonModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent {

  showEditMenu = false;

  showProfileMenu = false;

  showBillingMenu = false;

  showEditProfileMenu = false;

  constructor(public featureToggleService: FeatureToggleService) {}

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
