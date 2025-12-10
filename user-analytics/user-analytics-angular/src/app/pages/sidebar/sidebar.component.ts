import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

interface MenuItem {
  label?: string;
  title?: string;
  route?: string;
  icon: string;
  nested?: boolean;
  key?: string;
  children?: MenuItem[];
}

interface MenuSection {
  key?: string;
  title: string;
  route?: string;
  icon: string;
  children?: MenuItem[];
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  isCollapsed = false;
  expandedSections: { [key: string]: boolean } = {};

  menuSections: MenuSection[] = [
    // MAIN NAVIGATION
    { 
      title: 'Dashboard', 
      route: '/dashboard', 
      icon: 'bi-speedometer2' 
    },
    
    // PROFILE & ACCOUNT
    {
      key: 'profile',
      title: 'My Profile',
      icon: 'bi-person-circle',
      children: [
        { label: 'View Profile', route: '/profile', icon: 'bi-person' },
        { label: 'Edit Profile', route: '/editprofile', icon: 'bi-pencil' },
        { label: 'Personal Details', route: '/editdetailsprofile', icon: 'bi-card-list' },
        {
          label: 'Professional Info',
          route: '',
          nested: true,
          key: 'professional',
          icon: 'bi-briefcase',
          children: [
            { label: 'Skills', route: '/editskillsprofile', icon: 'bi-lightbulb' },
            { label: 'Experience', route: '/editexperienceprofile', icon: 'bi-briefcase' },
            { label: 'Education', route: '/editeducationprofile', icon: 'bi-mortarboard' },
            { label: 'Certificates', route: '/editcertificatesprofile', icon: 'bi-award' },
            { label: 'Projects', route: '/editprojectsprofile', icon: 'bi-kanban' }
          ]
        },
        {
          label: 'Billing & Payment',
          route: '',
          nested: true,
          key: 'billing',
          icon: 'bi-credit-card',
          children: [
            { label: 'Payment Methods', route: '/editcardprofile', icon: 'bi-credit-card-2-front' },
            { label: 'Add New Card', route: '/addcardprofile', icon: 'bi-plus-circle' }
          ]
        }
      ]
    },

    // PRODUCTIVITY
    {
      key: 'productivity',
      title: 'Productivity',
      icon: 'bi-list-check',
      children: [
        { label: 'Tasks', route: '/tasks', icon: 'bi-list-check' },
        { label: 'Calendar', route: '/calendar', icon: 'bi-calendar-event' },
        { label: 'Reminders', route: '/reminders', icon: 'bi-bell' },
        { label: 'File Manager', route: '/file-manager', icon: 'bi-folder' }
      ]
    },

    // USERS & MANAGEMENT
    {
      key: 'users',
      title: 'Users & Roles',
      icon: 'bi-people',
      children: [
        { label: 'All Users', route: '/users', icon: 'bi-people-fill' },
        { label: 'User Details', route: '/user-detail', icon: 'bi-person-lines-fill' },
        { label: 'User Roles', route: '/roles', icon: 'bi-shield-check' }
      ]
    },

    // ANALYTICS & REPORTS
    {
      key: 'analytics',
      title: 'Analytics & Reports',
      icon: 'bi-graph-up',
      children: [
        { label: 'Activity Timeline', route: '/activity', icon: 'bi-graph-up' },
        { label: 'Usage Insights', route: '/usage-insights', icon: 'bi-bar-chart' },
        { label: 'Reports', route: '/reports', icon: 'bi-file-earmark-bar-graph' },
        { label: 'Audit Logs', route: '/audit-logs', icon: 'bi-journal-text' }
      ]
    },

    // COMMUNICATIONS
    {
      key: 'communications',
      title: 'Communications',
      icon: 'bi-chat-dots',
      children: [
        { label: 'Notifications', route: '/notifications', icon: 'bi-bell-fill' },
        { label: 'Announcements', route: '/announcements', icon: 'bi-megaphone' },
        { label: 'AI Assistant', route: '/assistant', icon: 'bi-robot' }
      ]
    },

    // SYSTEM & ADMIN
    {
      key: 'admin',
      title: 'System & Admin',
      icon: 'bi-gear',
      children: [
        { label: 'Settings', route: '/settings', icon: 'bi-gear' },
        { label: 'Feature Toggles', route: '/feature-toggles', icon: 'bi-toggle-on' },
        { label: 'System Status', route: '/status', icon: 'bi-activity' },
        { label: 'Data Import/Export', route: '/data-import-export', icon: 'bi-arrow-left-right' }
      ]
    },

    // HELP & SUPPORT
    {
      key: 'help',
      title: 'Help & Support',
      icon: 'bi-question-circle',
      children: [
        { label: 'Documentation', route: '/docs', icon: 'bi-book' },
        { label: 'Tutorials', route: '/tutorials', icon: 'bi-journal-code' },
        { label: 'Support / Feedback', route: '/support', icon: 'bi-chat-dots' },
        { label: 'Language', route: '/language-switcher', icon: 'bi-translate' }
      ]
    },

    // LOGOUT (Always at bottom)
    { 
      title: 'Logout', 
      route: '/logout', 
      icon: 'bi-box-arrow-right' 
    }
  ];

  toggleSidebar() {
    this.isCollapsed = !this.isCollapsed;
  }

  toggleSection(key: string) {
    this.expandedSections[key] = !this.expandedSections[key];
  }

  isSectionExpanded(key: string): boolean {
    return !!this.expandedSections[key];
  }
}