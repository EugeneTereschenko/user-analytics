import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AppointmentListComponent } from './components/appointments/appointment-list/appointment-list.component';
import { AppointmentFormComponent } from './components/appointments/appointment-form/appointment-form.component';
import { AppointmentDetailComponent } from './components/appointments/appointment-detail/appointment-detail.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  {
    path: 'appointments',
    canActivate: [authGuard],
    children: [
      { path: '', component: AppointmentListComponent },
      { path: 'new', component: AppointmentFormComponent },
      { path: ':id', component: AppointmentDetailComponent },
      { path: ':id/edit', component: AppointmentFormComponent }
    ]
  },
  { path: '**', redirectTo: '/login' }
];
