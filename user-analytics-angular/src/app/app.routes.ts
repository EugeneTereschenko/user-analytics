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
  }

];
