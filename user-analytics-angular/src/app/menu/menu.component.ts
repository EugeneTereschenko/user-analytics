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
      icon: 'bi-pencil-square', // More distinctive edit icon
      children: [
        { label: 'Settings', route: '/settings', icon: 'bi-gear' },
        { label: 'Feature Toggles', route: '/feature-toggles', icon: 'bi-toggle-on' },
        { label: 'System Status', route: '/status', icon: 'bi-activity' },
        { label: '📊 Activity', route: '/activity', icon: 'bi-graph-up' },
        { label: 'Usage Insights', route: '/usage-insights', icon: 'bi-bar-chart' },
        { label: 'User Roles', route: '/roles', icon: 'bi-people' },
        { label: '🌐 Language Switcher', route: '/language-switcher', icon: 'bi-translate' },
        { label: 'Data Import/Export', route: '/data-import-export', icon: 'bi-arrow-left-right' },
        { label: 'File Manager', route: '/file-manager', icon: 'bi-folder' },
        { label: 'User Detail', route: '/user-detail', icon: 'bi-person-lines-fill' },
        { label: 'Audit Logs', route: '/audit-logs', icon: 'bi-journal-text' },
        { label: 'Users', route: '/users', icon: 'bi-people-fill' },
      ]
    },
    {
      key: 'profile',
      title: 'Profile',
      icon: 'bi-person-circle', // More visually distinct profile icon
      children: [
        { label: 'View Profile', route: '/profile', icon: 'bi-person' },
        { label: 'Edit Profile', route: '/editprofile', icon: 'bi-pencil' },
        { label: 'Edit Details', route: '/editdetailsprofile', icon: 'bi-card-list' },
        { label: 'Edit Skills', route: '/editskillsprofile', icon: 'bi-lightbulb' },
        { label: 'Edit Experience', route: '/editexperienceprofile', icon: 'bi-briefcase' },
        { label: 'Edit Education', route: '/editeducationprofile', icon: 'bi-mortarboard' },
        { label: 'Edit Certificates', route: '/editcertificatesprofile', icon: 'bi-award' },
        { label: 'Edit Projects', route: '/editprojectsprofile', icon: 'bi-kanban' },
        {
          label: 'Billing',
          route: '',
          nested: true,
          key: 'billing',
          icon: 'bi-credit-card',
          children: [
            { label: 'Add Card', route: '/addcardprofile', icon: 'bi-plus-circle' },
            { label: 'Edit Card', route: '/editcardprofile', icon: 'bi-pencil-square' }
          ]
        }
      ]
    },
    { title: 'Tutorials', route: '/tutorials', icon: 'bi-journal-code' },
    { title: '🔔 Reminders', route: '/reminders', icon: 'bi-bell' },
    { title: '📅 Calendar', route: '/calendar', icon: 'bi-calendar-event' },
    { title: 'Tasks', route: '/tasks', icon: 'bi-list-check' },
    { title: 'AI Assistant', route: '/assistant', icon: 'bi-robot' },
    { title: 'Notifications', route: '/notifications', icon: 'bi-bell-fill' },
    { title: 'Reports', route: '/reports', icon: 'bi-file-earmark-bar-graph' },
    { title: 'Announcements', route: '/announcements', icon: 'bi-megaphone' },
    { title: 'Support / Feedback', route: '/support', icon: 'bi-chat-dots' },
    { title: 'Logout', route: '/logout', icon: 'bi-box-arrow-right' }
  ];
}

