import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { DoctorService } from '../../../services/doctor.service';
import { Doctor } from '../../../models/doctor.model';

@Component({
  selector: 'app-doctor-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="container py-4">
      <div class="row justify-content-center">
        <div class="col-lg-10">
          <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
              <button class="btn btn-outline-secondary btn-sm mb-2" routerLink="/dashboard">
                <i class="bi bi-arrow-left me-2"></i>Back to Dashboard
              </button>
              <h2><i class="bi bi-person-badge me-2"></i>Doctor Profile</h2>
            </div>
            <button class="btn btn-outline-secondary" routerLink="/doctors">
              <i class="bi bi-arrow-left me-2"></i>Back to List
            </button>
          </div>

          <div *ngIf="loading" class="text-center py-5">
            <div class="spinner-border text-primary"></div>
          </div>

          <div *ngIf="!loading && doctor" class="row g-4">
            <!-- Profile Card -->
            <div class="col-md-4">
              <div class="card shadow-sm text-center">
                <div class="card-body p-4">
                  <div class="avatar-circle-lg bg-primary text-white mx-auto mb-3">
                    {{ getInitials(doctor.firstName, doctor.lastName) }}
                  </div>
                  <h4 class="mb-1">Dr. {{ doctor.firstName }} {{ doctor.lastName }}</h4>
                  <p class="text-muted mb-3">{{ doctor.specialization }}</p>
                  <span class="badge fs-6" [ngClass]="getStatusBadgeClass(doctor.status)">
                    {{ doctor.status }}
                  </span>

                  <div class="d-grid gap-2 mt-4">
                    <button class="btn btn-primary" [routerLink]="['/doctors', doctor.id, 'edit']">
                      <i class="bi bi-pencil me-2"></i>Edit Profile
                    </button>
                    <button class="btn btn-outline-danger" (click)="deleteDoctor()">
                      <i class="bi bi-trash me-2"></i>Delete
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Details Cards -->
            <div class="col-md-8">
              <!-- Contact Information -->
              <div class="card shadow-sm mb-3">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-person-lines-fill me-2"></i>Contact Information</h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="text-muted small">Email</label>
                      <p class="fw-semibold mb-0">{{ doctor.email }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Phone</label>
                      <p class="fw-semibold mb-0">{{ doctor.phoneNumber }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Date of Birth</label>
                      <p class="fw-semibold mb-0">{{ doctor.dateOfBirth || 'N/A' }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Gender</label>
                      <p class="fw-semibold mb-0">{{ doctor.gender }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Professional Information -->
              <div class="card shadow-sm mb-3">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-briefcase me-2"></i>Professional Information</h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="text-muted small">License Number</label>
                      <p class="fw-semibold mb-0">{{ doctor.licenseNumber || 'N/A' }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Department</label>
                      <p class="fw-semibold mb-0">{{ doctor.department || 'N/A' }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Years of Experience</label>
                      <p class="fw-semibold mb-0">{{ doctor.yearsOfExperience || 0 }} years</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Consultation Fee</label>
                      <p class="fw-semibold mb-0">\${{ doctor.consultationFee || 0 }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Room Number</label>
                      <p class="fw-semibold mb-0">{{ doctor.roomNumber || 'N/A' }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Joined Date</label>
                      <p class="fw-semibold mb-0">{{ doctor.joinedDate || 'N/A' }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Biography -->
              <div class="card shadow-sm mb-3" *ngIf="doctor.biography">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-file-text me-2"></i>Biography</h5>
                </div>
                <div class="card-body">
                  <p class="mb-0">{{ doctor.biography }}</p>
                </div>
              </div>

              <!-- Emergency Contact -->
              <div class="card shadow-sm" *ngIf="doctor.emergencyContactName">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-telephone-plus me-2"></i>Emergency Contact</h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="text-muted small">Name</label>
                      <p class="fw-semibold mb-0">{{ doctor.emergencyContactName }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Phone</label>
                      <p class="fw-semibold mb-0">{{ doctor.emergencyContactPhone }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .avatar-circle-lg {
      width: 120px;
      height: 120px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 48px;
      font-weight: bold;
    }
  `]
})
export class DoctorDetailComponent implements OnInit {
  private doctorService = inject(DoctorService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  doctor: Doctor | null = null;
  loading = false;

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.loadDoctor(id);
    }
  }

  loadDoctor(id: number): void {
    this.loading = true;
    this.doctorService.getDoctorById(id).subscribe({
      next: (doctor) => {
        this.doctor = doctor;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading doctor:', error);
        this.loading = false;
      }
    });
  }

  deleteDoctor(): void {
    if (this.doctor?.id && confirm('Are you sure you want to delete this doctor?')) {
      this.doctorService.deleteDoctor(this.doctor.id).subscribe({
        next: () => this.router.navigate(['/doctors']),
        error: (error) => console.error('Error deleting doctor:', error)
      });
    }
  }

  getInitials(firstName: string, lastName: string): string {
    return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
  }

  getStatusBadgeClass(status: string): string {
    const classes: { [key: string]: string } = {
      'ACTIVE': 'bg-success',
      'INACTIVE': 'bg-secondary',
      'ON_LEAVE': 'bg-warning',
      'SUSPENDED': 'bg-danger'
    };
    return classes[status] || 'bg-secondary';
  }
}
