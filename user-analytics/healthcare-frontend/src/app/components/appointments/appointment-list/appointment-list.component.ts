import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AppointmentService } from '../../../services/appointment.service';
import { Appointment, AppointmentStatus, AppointmentType } from '../../../models/appointment.model';

@Component({
  selector: 'app-appointment-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container-fluid py-4">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 class="mb-1"><i class="bi bi-calendar-check me-2"></i>Appointments</h2>
          <p class="text-muted mb-0">Manage and view all appointments</p>
        </div>
        <button class="btn btn-primary" (click)="navigateToCreate()">
          <i class="bi bi-plus-circle me-2"></i>New Appointment
        </button>
      </div>

      <!-- Filters -->
      <div class="card mb-4">
        <div class="card-body">
          <div class="row g-3">
            <div class="col-md-3">
              <label class="form-label small text-muted">Status</label>
              <select class="form-select" [(ngModel)]="filterStatus" (change)="applyFilters()">
                <option value="">All Statuses</option>
                <option *ngFor="let status of statuses" [value]="status">{{ status }}</option>
              </select>
            </div>
            <div class="col-md-3">
              <label class="form-label small text-muted">Start Date</label>
              <input type="date" class="form-control" [(ngModel)]="filterStartDate" (change)="applyFilters()">
            </div>
            <div class="col-md-3">
              <label class="form-label small text-muted">End Date</label>
              <input type="date" class="form-control" [(ngModel)]="filterEndDate" (change)="applyFilters()">
            </div>
            <div class="col-md-3 d-flex align-items-end">
              <button class="btn btn-outline-secondary w-100" (click)="clearFilters()">
                <i class="bi bi-x-circle me-2"></i>Clear Filters
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Quick Stats -->
      <div class="row g-3 mb-4">
        <div class="col-md-3">
          <div class="card border-0 shadow-sm bg-primary text-white">
            <div class="card-body">
              <h6 class="text-uppercase mb-2 opacity-75">Today's Appointments</h6>
              <h3 class="mb-0">{{ todaysCount }}</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm bg-success text-white">
            <div class="card-body">
              <h6 class="text-uppercase mb-2 opacity-75">Confirmed</h6>
              <h3 class="mb-0">{{ confirmedCount }}</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm bg-warning text-white">
            <div class="card-body">
              <h6 class="text-uppercase mb-2 opacity-75">Scheduled</h6>
              <h3 class="mb-0">{{ scheduledCount }}</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm bg-info text-white">
            <div class="card-body">
              <h6 class="text-uppercase mb-2 opacity-75">Completed</h6>
              <h3 class="mb-0">{{ completedCount }}</h3>
            </div>
          </div>
        </div>
      </div>

      <!-- Appointments Table -->
      <div class="card shadow-sm">
        <div class="card-body">
          <div *ngIf="loading" class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>
            <p class="text-muted mt-3">Loading appointments...</p>
          </div>

          <div *ngIf="!loading && appointments.length === 0" class="text-center py-5">
            <i class="bi bi-calendar-x display-1 text-muted"></i>
            <h5 class="mt-3 text-muted">No appointments found</h5>
            <p class="text-muted">Create your first appointment to get started.</p>
            <button class="btn btn-primary mt-3" (click)="navigateToCreate()">
              <i class="bi bi-plus-circle me-2"></i>Create Appointment
            </button>
          </div>

          <div *ngIf="!loading && appointments.length > 0" class="table-responsive">
            <table class="table table-hover align-middle">
              <thead class="table-light">
                <tr>
                  <th>Date & Time</th>
                  <th>Patient</th>
                  <th>Doctor</th>
                  <th>Type</th>
                  <th>Status</th>
                  <th>Duration</th>
                  <th class="text-center">Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let appointment of appointments">
                  <td>
                    <strong>{{ formatDate(appointment.appointmentDateTime) }}</strong><br>
                    <small class="text-muted">{{ formatTime(appointment.appointmentDateTime) }}</small>
                  </td>
                  <td>
                    <div>{{ appointment.patientName || 'N/A' }}</div>
                    <small class="text-muted">{{ appointment.patientEmail }}</small>
                  </td>
                  <td>
                    <div>{{ appointment.doctorName || 'N/A' }}</div>
                    <small class="text-muted">{{ appointment.doctorSpecialization }}</small>
                  </td>
                  <td>
                    <span class="badge bg-light text-dark">{{ appointment.appointmentType }}</span>
                  </td>
                  <td>
                    <span class="badge" [ngClass]="getStatusBadgeClass(appointment.status)">
                      {{ appointment.status }}
                    </span>
                  </td>
                  <td>{{ appointment.durationMinutes }} min</td>
                  <td class="text-center">
                    <div class="btn-group btn-group-sm" role="group">
                      <button class="btn btn-outline-primary" (click)="viewAppointment(appointment.id!)" title="View">
                        <i class="bi bi-eye"></i>
                      </button>
                      <button class="btn btn-outline-success" (click)="confirmAppointment(appointment.id!)" 
                              *ngIf="appointment.status === 'SCHEDULED'" title="Confirm">
                        <i class="bi bi-check-circle"></i>
                      </button>
                      <button class="btn btn-outline-info" (click)="editAppointment(appointment.id!)" title="Edit">
                        <i class="bi bi-pencil"></i>
                      </button>
                      <button class="btn btn-outline-danger" (click)="cancelAppointment(appointment.id!)" 
                              *ngIf="appointment.status !== 'CANCELLED' && appointment.status !== 'COMPLETED'" title="Cancel">
                        <i class="bi bi-x-circle"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Pagination -->
          <nav *ngIf="totalPages > 1" aria-label="Appointment pagination" class="mt-4">
            <ul class="pagination justify-content-center mb-0">
              <li class="page-item" [class.disabled]="currentPage === 0">
                <a class="page-link" (click)="goToPage(currentPage - 1)" href="javascript:void(0)">Previous</a>
              </li>
              <li class="page-item" *ngFor="let page of getPages()" [class.active]="page === currentPage">
                <a class="page-link" (click)="goToPage(page)" href="javascript:void(0)">{{ page + 1 }}</a>
              </li>
              <li class="page-item" [class.disabled]="currentPage === totalPages - 1">
                <a class="page-link" (click)="goToPage(currentPage + 1)" href="javascript:void(0)">Next</a>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>

    <!-- Cancel Modal -->
    <div class="modal fade" id="cancelModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Cancel Appointment</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label class="form-label">Cancellation Reason</label>
              <textarea class="form-control" rows="3" [(ngModel)]="cancellationReason" 
                        placeholder="Please provide a reason for cancellation..."></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            <button type="button" class="btn btn-danger" (click)="confirmCancel()">Cancel Appointment</button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .table th {
      font-size: 0.875rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
    .btn-group-sm .btn {
      padding: 0.25rem 0.5rem;
    }
  `]
})
export class AppointmentListComponent implements OnInit {
  private appointmentService = inject(AppointmentService);
  private router = inject(Router);

  appointments: Appointment[] = [];
  loading = false;
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;

  filterStatus = '';
  filterStartDate = '';
  filterEndDate = '';

  statuses = Object.values(AppointmentStatus);
  
  todaysCount = 0;
  confirmedCount = 0;
  scheduledCount = 0;
  completedCount = 0;

  cancellationReason = '';
  selectedAppointmentId: number | null = null;

  ngOnInit(): void {
    this.loadAppointments();
    this.loadTodaysAppointments();
  }

  loadAppointments(): void {
    this.loading = true;
    this.appointmentService.getAllAppointments(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.appointments = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.updateCounts();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading appointments:', error);
        this.loading = false;
      }
    });
  }

  loadTodaysAppointments(): void {
    this.appointmentService.getTodaysAppointments().subscribe({
      next: (appointments) => {
        this.todaysCount = appointments.length;
      }
    });
  }

  updateCounts(): void {
    this.confirmedCount = this.appointments.filter(a => a.status === AppointmentStatus.CONFIRMED).length;
    this.scheduledCount = this.appointments.filter(a => a.status === AppointmentStatus.SCHEDULED).length;
    this.completedCount = this.appointments.filter(a => a.status === AppointmentStatus.COMPLETED).length;
  }

  applyFilters(): void {
    if (this.filterStartDate && this.filterEndDate) {
      this.appointmentService.getAppointmentsBetween(
        this.filterStartDate + 'T00:00:00',
        this.filterEndDate + 'T23:59:59'
      ).subscribe({
        next: (appointments) => {
          this.appointments = this.filterStatus 
            ? appointments.filter(a => a.status === this.filterStatus)
            : appointments;
        }
      });
    } else if (this.filterStatus) {
      this.appointmentService.getAppointmentsByStatus(
        this.filterStatus as AppointmentStatus,
        this.currentPage,
        this.pageSize
      ).subscribe({
        next: (response) => {
          this.appointments = response.content;
          this.totalPages = response.totalPages;
        }
      });
    } else {
      this.loadAppointments();
    }
  }

  clearFilters(): void {
    this.filterStatus = '';
    this.filterStartDate = '';
    this.filterEndDate = '';
    this.loadAppointments();
  }

  confirmAppointment(id: number): void {
    this.appointmentService.confirmAppointment(id).subscribe({
      next: () => {
        this.loadAppointments();
      },
      error: (error) => console.error('Error confirming appointment:', error)
    });
  }

  cancelAppointment(id: number): void {
    this.selectedAppointmentId = id;
    this.cancellationReason = '';
    const modal = new (window as any).bootstrap.Modal(document.getElementById('cancelModal'));
    modal.show();
  }

  confirmCancel(): void {
    if (this.selectedAppointmentId) {
      this.appointmentService.cancelAppointment(this.selectedAppointmentId, this.cancellationReason).subscribe({
        next: () => {
          const modal = (window as any).bootstrap.Modal.getInstance(document.getElementById('cancelModal'));
          modal.hide();
          this.loadAppointments();
        },
        error: (error) => console.error('Error cancelling appointment:', error)
      });
    }
  }

  viewAppointment(id: number): void {
    this.router.navigate(['/appointments', id]);
  }

  editAppointment(id: number): void {
    this.router.navigate(['/appointments', id, 'edit']);
  }

  navigateToCreate(): void {
    this.router.navigate(['/appointments', 'new']);
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadAppointments();
    }
  }

  getPages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  formatDate(dateTime: string): string {
    return new Date(dateTime).toLocaleDateString('en-US', {
      weekday: 'short',
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }

  formatTime(dateTime: string): string {
    return new Date(dateTime).toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit'
    });
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
