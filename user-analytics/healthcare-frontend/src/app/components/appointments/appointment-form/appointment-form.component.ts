import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AppointmentService } from '../../../services/appointment.service';
import { AuthService } from '../../../services/auth.service';
import { AppointmentType, AppointmentStatus } from '../../../models/appointment.model';

@Component({
  selector: 'app-appointment-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="container py-4">
      <div class="row justify-content-center">
        <div class="col-lg-8">
          <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
              <h4 class="mb-0">
                <i class="bi bi-calendar-plus me-2"></i>
                {{ isEditMode ? 'Edit Appointment' : 'New Appointment' }}
              </h4>
            </div>
            <div class="card-body p-4">
              <form [formGroup]="appointmentForm" (ngSubmit)="onSubmit()">
                <div class="row g-3">
                  <!-- Patient Information -->
                  <div class="col-12">
                    <h5 class="border-bottom pb-2 mb-3">
                      <i class="bi bi-person me-2"></i>Patient Information
                    </h5>
                  </div>

                  <div class="col-md-6">
                    <label class="form-label">Patient ID <span class="text-danger">*</span></label>
                    <input type="number" class="form-control" formControlName="patientId"
                           [class.is-invalid]="submitted && f['patientId'].errors">
                    <div *ngIf="submitted && f['patientId'].errors" class="invalid-feedback">
                      <div *ngIf="f['patientId'].errors['required']">Patient ID is required</div>
                    </div>
                  </div>

                  <div class="col-md-6">
                    <label class="form-label">Patient Name</label>
                    <input type="text" class="form-control" formControlName="patientName"
                           placeholder="John Doe">
                  </div>

                  <div class="col-md-6">
                    <label class="form-label">Patient Email</label>
                    <input type="email" class="form-control" formControlName="patientEmail"
                           placeholder="patient@example.com">
                  </div>

                  <div class="col-md-6">
                    <label class="form-label">Patient Phone</label>
                    <input type="tel" class="form-control" formControlName="patientPhone"
                           placeholder="+1234567890">
                  </div>

                  <!-- Doctor Information -->
                  <div class="col-12 mt-4">
                    <h5 class="border-bottom pb-2 mb-3">
                      <i class="bi bi-person-badge me-2"></i>Doctor Information
                    </h5>
                  </div>

                  <div class="col-md-6">
                    <label class="form-label">Doctor ID <span class="text-danger">*</span></label>
                    <input type="number" class="form-control" formControlName="doctorId"
                           [class.is-invalid]="submitted && f['doctorId'].errors">
                    <div *ngIf="submitted && f['doctorId'].errors" class="invalid-feedback">
                      <div *ngIf="f['doctorId'].errors['required']">Doctor ID is required</div>
                    </div>
                  </div>

                  <div class="col-md-6">
                    <label class="form-label">Doctor Name</label>
                    <input type="text" class="form-control" formControlName="doctorName"
                           placeholder="Dr. Smith">
                  </div>

                  <div class="col-md-12">
                    <label class="form-label">Doctor Specialization</label>
                    <input type="text" class="form-control" formControlName="doctorSpecialization"
                           placeholder="Cardiology">
                  </div>

                  <!-- Appointment Details -->
                  <div class="col-12 mt-4">
                    <h5 class="border-bottom pb-2 mb-3">
                      <i class="bi bi-calendar-event me-2"></i>Appointment Details
                    </h5>
                  </div>

                  <div class="col-md-6">
                    <label class="form-label">Date & Time <span class="text-danger">*</span></label>
                    <input type="datetime-local" class="form-control" formControlName="appointmentDateTime"
                           [class.is-invalid]="submitted && f['appointmentDateTime'].errors">
                    <div *ngIf="submitted && f['appointmentDateTime'].errors" class="invalid-feedback">
                      <div *ngIf="f['appointmentDateTime'].errors['required']">Date & time is required</div>
                    </div>
                  </div>

                  <div class="col-md-6">
                    <label class="form-label">Duration (minutes) <span class="text-danger">*</span></label>
                    <select class="form-select" formControlName="durationMinutes"
                            [class.is-invalid]="submitted && f['durationMinutes'].errors">
                      <option value="15">15 minutes</option>
                      <option value="30">30 minutes</option>
                      <option value="45">45 minutes</option>
                      <option value="60">60 minutes</option>
                      <option value="90">90 minutes</option>
                      <option value="120">120 minutes</option>
                    </select>
                  </div>

                  <div class="col-md-6">
                    <label class="form-label">Appointment Type <span class="text-danger">*</span></label>
                    <select class="form-select" formControlName="appointmentType"
                            [class.is-invalid]="submitted && f['appointmentType'].errors">
                      <option value="">Select type</option>
                      <option *ngFor="let type of appointmentTypes" [value]="type">{{ formatType(type) }}</option>
                    </select>
                    <div *ngIf="submitted && f['appointmentType'].errors" class="invalid-feedback">
                      <div *ngIf="f['appointmentType'].errors['required']">Appointment type is required</div>
                    </div>
                  </div>

                  <div class="col-md-6" *ngIf="isEditMode">
                    <label class="form-label">Status</label>
                    <select class="form-select" formControlName="status">
                      <option *ngFor="let status of statuses" [value]="status">{{ status }}</option>
                    </select>
                  </div>

                  <div class="col-12">
                    <label class="form-label">Reason for Visit</label>
                    <textarea class="form-control" formControlName="reason" rows="3"
                              placeholder="Describe the reason for this appointment..."></textarea>
                  </div>

                  <div class="col-12" *ngIf="isEditMode">
                    <label class="form-label">Notes</label>
                    <textarea class="form-control" formControlName="notes" rows="3"
                              placeholder="Additional notes..."></textarea>
                  </div>
                </div>

                <div *ngIf="error" class="alert alert-danger mt-3" role="alert">
                  <i class="bi bi-exclamation-triangle-fill me-2"></i>{{ error }}
                </div>

                <div *ngIf="success" class="alert alert-success mt-3" role="alert">
                  <i class="bi bi-check-circle-fill me-2"></i>{{ success }}
                </div>

                <div class="d-flex gap-2 mt-4">
                  <button type="submit" class="btn btn-primary" [disabled]="loading">
                    <span *ngIf="loading" class="spinner-border spinner-border-sm me-2"></span>
                    <i *ngIf="!loading" class="bi bi-check-circle me-2"></i>
                    {{ loading ? 'Saving...' : (isEditMode ? 'Update Appointment' : 'Create Appointment') }}
                  </button>
                  <button type="button" class="btn btn-outline-secondary" (click)="cancel()">
                    <i class="bi bi-x-circle me-2"></i>Cancel
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class AppointmentFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private appointmentService = inject(AppointmentService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  appointmentForm: FormGroup;
  submitted = false;
  loading = false;
  error = '';
  success = '';
  isEditMode = false;
  appointmentId: number | null = null;

  appointmentTypes = Object.values(AppointmentType);
  statuses = Object.values(AppointmentStatus);

  constructor() {
    this.appointmentForm = this.fb.group({
      patientId: ['', Validators.required],
      doctorId: ['', Validators.required],
      userId: [''],
      appointmentDateTime: ['', Validators.required],
      durationMinutes: [30, Validators.required],
      status: [AppointmentStatus.SCHEDULED],
      appointmentType: ['', Validators.required],
      reason: [''],
      notes: [''],
      patientName: [''],
      patientEmail: [''],
      patientPhone: [''],
      doctorName: [''],
      doctorSpecialization: ['']
    });
  }

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.appointmentForm.patchValue({ userId: currentUser.id });
    }

    this.appointmentId = this.route.snapshot.params['id'];
    if (this.appointmentId) {
      this.isEditMode = true;
      this.loadAppointment(this.appointmentId);
    }
  }

  get f() {
    return this.appointmentForm.controls;
  }

  loadAppointment(id: number): void {
    this.loading = true;
    this.appointmentService.getAppointmentById(id).subscribe({
      next: (appointment) => {
        this.appointmentForm.patchValue({
          ...appointment,
          appointmentDateTime: this.formatDateTimeForInput(appointment.appointmentDateTime)
        });
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load appointment';
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';
    this.success = '';

    if (this.appointmentForm.invalid) {
      return;
    }

    this.loading = true;
    const formValue = this.appointmentForm.value;

    if (this.isEditMode && this.appointmentId) {
      this.appointmentService.updateAppointment(this.appointmentId, formValue).subscribe({
        next: () => {
          this.success = 'Appointment updated successfully!';
          setTimeout(() => this.router.navigate(['/appointments']), 2000);
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to update appointment';
          this.loading = false;
        }
      });
    } else {
      this.appointmentService.createAppointment(formValue).subscribe({
        next: () => {
          this.success = 'Appointment created successfully!';
          setTimeout(() => this.router.navigate(['/appointments']), 2000);
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to create appointment';
          this.loading = false;
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/appointments']);
  }

  formatDateTimeForInput(dateTime: string): string {
    const date = new Date(dateTime);
    return date.toISOString().slice(0, 16);
  }

  formatType(type: string): string {
    return type.replace(/_/g, ' ').toLowerCase()
      .replace(/\b\w/g, c => c.toUpperCase());
  }
}
