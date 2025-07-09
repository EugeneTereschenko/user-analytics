import { Routes } from '@angular/router';
import { authGuard } from './auth/auth.guard';


export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  {
  path: 'dashboard',
  canActivate: [authGuard],
  loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'users',
    loadComponent: () =>
      import('./pages/user-list/user-list.component').then(m => m.UserListComponent)
  },
  {
    path: 'reports',
    loadComponent: () =>
      import('./pages/reports/reports.component').then(m => m.ReportsComponent)
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
  loadComponent: () =>
    import('./pages/documentation/documentation.component').then(m => m.DocumentationComponent)
  },
  {
  path: 'feature-toggles',
  loadComponent: () =>
    import('./pages/feature-toggle/feature-toggle.component').then(m => m.FeatureToggleComponent)
  }





];
