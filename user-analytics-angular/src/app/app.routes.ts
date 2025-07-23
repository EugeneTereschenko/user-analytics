import { Routes } from '@angular/router';
import { authGuard } from './auth/auth.guard';


export const routes: Routes = [
  {
  path: '',
  canActivate: [authGuard],
  loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
  path: 'dashboard',
  canActivate: [authGuard],
  loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
  path: 'users',
  canActivate: [authGuard],
  loadComponent: () => import('./pages/user-list/user-list.component').then(m => m.UserListComponent)
  },
  {
  path: 'reports',
  canActivate: [authGuard],
  loadComponent: () => import('./pages/reports/reports.component').then(m => m.ReportsComponent)
  },
  {
  path: 'signin',
  loadComponent: () =>
    import('./pages/signin/signin.component').then(m => m.SigninComponent)
  },
  {
  path: 'signup',
  loadComponent: () =>
    import('./pages/signup/signup.component').then(m => m.SignupComponent)
  },
  {
    path: 'logout',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/logout/logout.component').then(m => m.LogoutComponent)
  },
  {
  path: 'profile',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/profile/profile.component').then(m => m.ProfileComponent)
  },
  {
  path: 'editprofile',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/edit-profile/edit-profile.component').then(m => m.EditProfileComponent)
  },
  {
  path: 'editdetailsprofile',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/edit-details-profile/edit-details-profile.component').then(m => m.EditDetailsProfileComponent)
  },
  {
  path: 'editskillsprofile',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/edit-skills-profile/edit-skills-profile.component').then(m => m.EditSkillsProfileComponent)
  },
  {
  path: 'editcertificatesprofile',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/edit-certificates-profile/edit-certificates-profile.component').then(m => m.EditCertificatesProfileComponent) 
  },
  {
  path: 'editeducationprofile',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/edit-education-profile/edit-education-profile.component').then(m => m.EditEducationProfileComponent)
  },
  {
   path: 'editexperienceprofile',
   canActivate: [authGuard],
   loadComponent: () =>
     import('./pages/edit-experience-profile/edit-experience-profile.component').then(m => m.EditExperienceProfileComponent)
  },
  {
  path: 'editprojectsprofile',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/edit-projects-profile/edit-projects-profile.component').then(m => m.EditProjectsProfileComponent)
  },
  {
    path: 'editcardprofile',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/card-profile/card-profile.component').then(m => m.CardProfileComponent)
  },
  {
    path: 'addcardprofile',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/add-card-profile/add-card-profile.component').then(m => m.AddCardProfileComponent)
  },
  {
  path: 'settings',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/settings/settings.component').then(m => m.SettingsComponent)
  },
  {
  path: 'docs',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/documentation/documentation.component').then(m => m.DocumentationComponent)
  },
  {
  path: 'feature-toggles',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/feature-toggle/feature-toggle.component').then(m => m.FeatureToggleComponent)
  },
  {
  path: 'support',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/support/support.component').then(m => m.SupportComponent)
  },
  {
  path: 'audit-logs',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/audit-logs/audit-logs.component').then(m => m.AuditLogsComponent)
  },
  {
  path: 'notifications',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/notifications/notifications.component').then(m => m.NotificationsComponent)
  },
  {
  path: 'users/:id',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/user-detail/user-detail.component').then(m => m.UserDetailComponent)
  },
  {
  path: 'user-detail',
  canActivate: [authGuard],
  loadComponent: () =>
      import('./pages/user-detail/user-detail.component').then(m => m.UserDetailComponent)
  },
  {
    path: 'tasks',
    canActivate: [authGuard],
    loadComponent: () => 
      import('./pages/tasks/tasks.component').then(m => m.TasksComponent)
  },
  {
  path: 'calendar',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/calendar/calendar.component').then(m => m.CalendarComponent)
  },
  {
  path: 'reminders',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/reminders/reminders.component').then(m => m.RemindersComponent)
  },
  {
  path: 'activity',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/user-activity-timeline/user-activity-timeline.component').then(m => m.UserActivityTimelineComponent)
  },
  {
    path: 'language-switcher',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/language-switcher/language-switcher.component').then(m => m.LanguageSwitcherComponent)
  },
  {
  path: 'tutorials',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/tutorials/tutorials.component').then(m => m.TutorialsComponent)
  },
  {
  path: 'file-manager',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/file-manager/file-manager.component').then(m => m.FileManagerComponent)
  },
  {
    path: 'data-import-export',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/data-import-export/data-import-export.component').then(m => m.DataImportExportComponent)
  },
  {
  path: 'roles',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/roles/roles.component').then(m => m.RolesComponent)
  },
  {
  path: 'status',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/status/status.component').then(m => m.StatusComponent)
  },
  {
    path: 'assistant',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/assistant/assistant.component').then(m => m.AssistantComponent)
  }


];
