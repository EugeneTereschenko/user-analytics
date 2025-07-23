import { Component } from '@angular/core';
import { FeatureToggleService } from '../services/feature-toggle.service'; // Import the feature toggle service
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
  openSections: { [key: string]: boolean } = {};

  constructor(public featureToggleService: FeatureToggleService) {}

  toggleSection(key: string) {
    this.openSections[key] = !this.openSections[key];
  }

  nestedOpenSections: { [key: string]: boolean } = {};

  toggleNestedSection(key: string) {
    this.nestedOpenSections[key] = !this.nestedOpenSections[key];
  }

  isNestedSectionOpen(key: string): boolean {
    return this.nestedOpenSections[key];
  }


  isSectionOpen(key: string): boolean {
    return this.openSections[key];
  }

  menuSections = [
    {
      key: 'edit',
      title: 'Edit',
      icon: 'bi-pencil',
      children: [
        { label: 'Settings', route: '/settings' },
        { label: 'Feature Toggles', route: '/feature-toggles' },
        { label: 'Notifications', route: '/notifications' }
      ]
    },
    {
      key: 'profile',
      title: 'Profile',
      icon: 'bi-person',
      children: [
        { label: 'View Profile', route: '/profile' },
        { label: 'Edit Profile', route: '/editprofile' },
        { label: 'Edit Details', route: '/editdetailsprofile' },
        { label: 'Edit Skills', route: '/editskillsprofile' },
        { label: 'Edit Experience', route: '/editexperienceprofile' },
        { label: 'Edit Education', route: '/editeducationprofile' },
        { label: 'Edit Certificates', route: '/editcertificatesprofile' },
        { label: 'Edit Projects', route: '/editprojectsprofile' },
        {
          label: 'Billing',
          route: '',
          nested: true,
          key: 'billing',
          children: [
            { label: 'Add Card', route: '/addcardprofile' },
            { label: 'Edit Card', route: '/editcardprofile' }
          ]
        }
      ]
    },
    { title: 'User Detail', route: '/user-detail' },
    { title: 'Audit Logs', route: '/audit-logs' },
    { title: 'Users', route: '/users' },
    { title: 'Reports', route: '/reports' },
    { title: 'Announcements', route: '/announcements' },
    { title: 'Usage Insights', route: '/usage-insights' },
    { title: 'AI Assistant', route: '/assistant' },
    { title: 'System Status', route: '/status' },
    { title: 'User Roles', route: '/roles' },
    { title: 'Data Import/Export', route: '/data-import-export' },
    { title: 'File Manager', route: '/file-manager' },
    { title: 'Tutorials', route: '/tutorials' },
    { title: '🌐 Language Switcher', route: '/language-switcher' },
    { title: '📊 Activity', route: '/activity' },
    { title: '🔔 Reminders', route: '/reminders' },
    { title: '📅 Calendar', route: '/calendar' },
    { title: 'Tasks', route: '/tasks' },
    { title: 'Support / Feedback', route: '/support' },
    { title: 'Logout', route: '/logout' }
  ];
}

