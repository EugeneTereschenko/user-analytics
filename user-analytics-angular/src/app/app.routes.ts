import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
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
  }

];