import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AppointmentListComponent } from './components/appointments/appointment-list/appointment-list.component';
import { AppointmentFormComponent } from './components/appointments/appointment-form/appointment-form.component';
import { AppointmentDetailComponent } from './components/appointments/appointment-detail/appointment-detail.component';
import { DoctorListComponent } from './components/doctors/doctor-list/doctor-list.component';
import { DoctorFormComponent } from './components/doctors/doctor-form/doctor-form.component';
import { DoctorDetailComponent } from './components/doctors/doctor-detail/doctor-detail.component';
import { StaffListComponent } from './components/staff/staff-list/staff-list.component';
import { StaffFormComponent } from './components/staff/staff-form/staff-form.component';
import { StaffDetailComponent } from './components/staff/staff-detail/staff-detail.component';
import { PatientListComponent } from './components/patients/patient-list/patient-list.component';
import { PatientFormComponent } from './components/patients/patient-form/patient-form.component';
import { PatientDetailComponent } from './components/patients/patient-detail/patient-detail.component';
import { PrescriptionListComponent } from './components/prescriptions/prescription-list/prescription-list.component';
import { PrescriptionFormComponent } from './components/prescriptions/prescription-form/prescription-form.component';
import { InvoiceListComponent } from './components/billing/invoice-list/invoice-list.component';
import { InvoiceFormComponent } from './components/billing/invoice-form/invoice-form.component';
import { BillingDashboardComponent } from './components/billing/billing-dashboard/billing-dashboard.component';
import { NotificationListComponent } from './components/notifications/notification-list/notification-list.component';
import { NotificationCreateComponent } from './components/notifications/notification-create/notification-create.component';
import { MedicalRecordListComponent } from './components/medical-records/medical-record-list/medical-record-list.component';
import { MedicalRecordDetailComponent } from './components/medical-records/medical-record-detail/medical-record-detail.component';
import { MedicalRecordFormComponent } from './components/medical-records/medical-record-form/medical-record-form.component';
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
  {
    path: 'doctors',
    canActivate: [authGuard],
    children: [
      { path: '', component: DoctorListComponent },
      { path: 'new', component: DoctorFormComponent },
      { path: ':id', component: DoctorDetailComponent },
      { path: ':id/edit', component: DoctorFormComponent }
    ]
  },
  {
    path: 'staff',
    canActivate: [authGuard],
    children: [
      { path: '', component: StaffListComponent },
      { path: 'new', component: StaffFormComponent },
      { path: ':id', component: StaffDetailComponent },
      { path: ':id/edit', component: StaffFormComponent }
    ]
  },
  {
    path: 'patients',
    canActivate: [authGuard],
    children: [
      { path: '', component: PatientListComponent },
      { path: 'new', component: PatientFormComponent },
      { path: ':id', component: PatientDetailComponent },
      { path: ':id/edit', component: PatientFormComponent }
    ]
  },
  {
    path: 'prescriptions',
    canActivate: [authGuard],
    children: [
      { path: '', component: PrescriptionListComponent },
      { path: 'create', component: PrescriptionFormComponent },
      { path: 'edit/:id', component: PrescriptionFormComponent }
    ]
  },
  {
    path: 'billing',
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: BillingDashboardComponent },
      { path: 'invoices', component: InvoiceListComponent },
      { path: 'invoices/create', component: InvoiceFormComponent },
      { path: 'invoices/edit/:id', component: InvoiceFormComponent }
    ]
  },
  {
    path: 'notifications',
    canActivate: [authGuard],
    children: [
      { path: '', component: NotificationListComponent },
      { path: 'create', component: NotificationCreateComponent }
    ]
  },
  {
    path: 'medical-records',
    canActivate: [authGuard],
    children: [
      { path: '', component: MedicalRecordListComponent },
      { path: 'new', component: MedicalRecordFormComponent },
      { path: ':id', component: MedicalRecordDetailComponent },
      { path: ':id/edit', component: MedicalRecordFormComponent }
    ]
  },
  { path: '**', redirectTo: '/login' }
];
