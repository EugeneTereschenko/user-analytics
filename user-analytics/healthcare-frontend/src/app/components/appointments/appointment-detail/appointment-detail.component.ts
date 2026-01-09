import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { AppointmentService } from '../../../services/appointment.service';
import { Appointment } from '../../../models/appointment.model';

@Component({
  selector: 'app-appointment-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="container py-4">
      <div class="row justify-content-center">
        <div class="col-lg-10">
          <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="bi bi-calendar-check me-2"></i>Appointment Details</h2>
            <button class="btn btn-outline-secondary" routerLink="/appointments">
              <i class="bi bi-arrow-left me-2"></i>Back to List
            </button>
          </div>

          <div *ngIf="loading" class="text-center py-5">
            <div class="spinner-border text-primary" role="status"></div>
          </div>

          <div *ngIf="!loading && appointment" class="row g-4">
            <!-- Status Card -->
            <div class="col-md-4">
              <div class="card shadow-sm h-100">
                <div class="card-body text-center">
                  <div class="mb-3">
                    <span class="badge fs-5" [ngClass]="getStatusBadgeClass(appointment.status)">
                      {{ appointment.status }}
                    </span>
                  </div>
                  <h5 class="card-title">{{ formatType(appointment.appointmentType) }}</h5>
                  <p class="text-muted">{{ appointment.durationMinutes }} minutes</p>
                  
                  <div class="d-grid gap-2 mt-4">
                    <button class="btn btn-success" *ngIf="appointment.status === 'SCHEDULED'"
                            (click)="confirmAppointment()">
                      <i class="bi bi-check-circle me-2"></i>Confirm
                    </button>
                    <button class="btn btn-primary" *ngIf="appointment.status === 'CONFIRMED'"
                            (click)="completeAppointment()">
                      <i class="bi bi-check-circle-fill me-2"></i>Complete
                    </button>
                    <button class="btn btn-outline-danger" 
                            *ngIf="appointment.status !== 'CANCELLED' && appointment.status !== 'COMPLETED'"
                            (click)="cancelAppointment()">
                      <i class="bi bi-x-circle me-2"></i>Cancel
                    </button>
                    <button class="btn btn-outline-primary" [routerLink]="['/appointments', appointment.id, 'edit']">
                      <i class="bi bi-pencil me-2"></i>Edit
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Details Card -->
            <div class="col-md-8">
              <div class="card shadow-sm">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-info-circle me-2"></i>Appointment Information</h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="text-muted small">Date</label>
                      <p class="fw-semibold mb-0">{{ formatDate(appointment.appointmentDateTime) }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Time</label>
                      <p class="fw-semibold mb-0">{{ formatTime(appointment.appointmentDateTime) }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card shadow-sm mt-3">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-person me-2"></i>Patient Information</h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="text-muted small">Patient ID</label>
                      <p class="fw-semibold mb-0">{{ appointment.patientId }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Name</label>
                      <p class="fw-semibold mb-0">{{ appointment.patientName || 'N/A' }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Email</label>
                      <p class="fw-semibold mb-0">{{ appointment.patientEmail || 'N/A' }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Phone</label>
                      <p class="fw-semibold mb-0">{{ appointment.patientPhone || 'N/A' }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card shadow-sm mt-3">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-person-badge me-2"></i>Doctor Information</h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="text-muted small">Doctor ID</label>
                      <p class="fw-semibold mb-0">{{ appointment.doctorId }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Name</label>
                      <p class="fw-semibold mb-0">{{ appointment.doctorName || 'N/A' }}</p>
                    </div>
                    <div class="col-md-12">
                      <label class="text-muted small">Specialization</label>
                      <p class="fw-semibold mb-0">{{ appointment.doctorSpecialization || 'N/A' }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card shadow-sm mt-3" *ngIf="appointment.reason">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-file-text me-2"></i>Reason for Visit</h5>
                </div>
                <div class="card-body">
                  <p class="mb-0">{{ appointment.reason }}</p>
                </div>
              </div>

              <div class="card shadow-sm mt-3" *ngIf="appointment.notes">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-journal-text me-2"></i>Notes</h5>
                </div>
                <div class="card-body">
                  <p class="mb-0">{{ appointment.notes }}</p>
                </div>
              </div>

              <div class="card shadow-sm mt-3" *ngIf="appointment.cancelledAt">
                <div class="card-header bg-danger text-white">
                  <h5 class="mb-0"><i class="bi bi-x-circle me-2"></i>Cancellation Details</h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="text-muted small">Cancelled At</label>
                      <p class="fw-semibold mb-0">{{ formatDateTime(appointment.cancelledAt) }}</p>
                    </div>
                    <div class="col-md-12" *ngIf="appointment.cancellationReason">
                      <label class="text-muted small">Reason</label>
                      <p class="mb-0">{{ appointment.cancellationReason }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class AppointmentDetailComponent implements OnInit {
  private appointmentService = inject(AppointmentService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  appointment: Appointment | null = null;
  loading = false;

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.loadAppointment(id);
    }
  }

  loadAppointment(id: number): void {
    this.loading = true;
    this.appointmentService.getAppointmentById(id).subscribe({
      next: (appointment) => {
        this.appointment = appointment;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading appointment:', error);
        this.loading = false;
      }
    });
  }

  confirmAppointment(): void {
    if (this.appointment?.id) {
      this.appointmentService.confirmAppointment(this.appointment.id).subscribe({
        next: () => this.loadAppointment(this.appointment!.id!),
        error: (error) => console.error('Error confirming appointment:', error)
      });
    }
  }

  completeAppointment(): void {
    if (this.appointment?.id) {
      this.appointmentService.completeAppointment(this.appointment.id).subscribe({
        next: () => this.loadAppointment(this.appointment!.id!),
        error: (error) => console.error('Error completing appointment:', error)
      });
    }
  }

  cancelAppointment(): void {
    if (this.appointment?.id && confirm('Are you sure you want to cancel this appointment?')) {
      this.appointmentService.cancelAppointment(this.appointment.id).subscribe({
        next: () => this.loadAppointment(this.appointment!.id!),
        error: (error) => console.error('Error cancelling appointment:', error)
      });
    }
  }

  formatDate(dateTime: string): string {
    return new Date(dateTime).toLocaleDateString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  formatTime(dateTime: string): string {
    return new Date(dateTime).toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  formatDateTime(dateTime: string): string {
    return new Date(dateTime).toLocaleString('en-US');
  }

  formatType(type: string): string {
    return type.replace(/_/g, ' ').toLowerCase()
      .replace(/\b\w/g, c => c.toUpperCase());
  }

  getStatusBadgeClass(status: string): string {
    const classes: { [key: string]: string } = {
      'SCHEDULED': 'bg-warning',
      'CONFIRMED': 'bg-success',
      'IN_PROGRESS': 'bg-info',
      'COMPLETED': 'bg-primary',
      'CANCELLED': 'bg-danger',
      'NO_SHOW': 'bg-secondary',
      'RESCHEDULED': 'bg-info'
    };
    return classes[status] || 'bg-secondary';
  }
}
