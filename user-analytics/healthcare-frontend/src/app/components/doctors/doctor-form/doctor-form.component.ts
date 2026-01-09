import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { DoctorService } from '../../../services/doctor.service';
import { AuthService } from '../../../services/auth.service';
import { DoctorStatus, Gender } from '../../../models/doctor.model';

@Component({
  selector: 'app-doctor-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="container py-4">
      <div class="row justify-content-center">
        <div class="col-lg-10">
          <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
              <h4 class="mb-0">
                <i class="bi bi-person-plus me-2"></i>
                {{ isEditMode ? 'Edit Doctor' : 'Add New Doctor' }}
              </h4>
            </div>
            <div class="card-body p-4">
              <form [formGroup]="doctorForm" (ngSubmit)="onSubmit()">
                <!-- Personal Information -->
                <h5 class="border-bottom pb-2 mb-3">
                  <i class="bi bi-person me-2"></i>Personal Information
                </h5>
                <div class="row g-3 mb-4">
                  <div class="col-md-6">
                    <label class="form-label">First Name <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" formControlName="firstName"
                           [class.is-invalid]="submitted && f['firstName'].errors">
                    <div *ngIf="submitted && f['firstName'].errors" class="invalid-feedback">
                      First name is required
                    </div>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Last Name <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" formControlName="lastName"
                           [class.is-invalid]="submitted && f['lastName'].errors">
                    <div *ngIf="submitted && f['lastName'].errors" class="invalid-feedback">
                      Last name is required
                    </div>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Email <span class="text-danger">*</span></label>
                    <input type="email" class="form-control" formControlName="email"
                           [class.is-invalid]="submitted && f['email'].errors">
                    <div *ngIf="submitted && f['email'].errors" class="invalid-feedback">
                      Valid email is required
                    </div>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Phone Number <span class="text-danger">*</span></label>
                    <input type="tel" class="form-control" formControlName="phoneNumber"
                           [class.is-invalid]="submitted && f['phoneNumber'].errors">
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Date of Birth</label>
                    <input type="date" class="form-control" formControlName="dateOfBirth">
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Gender <span class="text-danger">*</span></label>
                    <select class="form-select" formControlName="gender"
                            [class.is-invalid]="submitted && f['gender'].errors">
                      <option value="">Select gender</option>
                      <option *ngFor="let g of genders" [value]="g">{{ g }}</option>
                    </select>
                  </div>
                </div>

                <!-- Professional Information -->
                <h5 class="border-bottom pb-2 mb-3">
                  <i class="bi bi-briefcase me-2"></i>Professional Information
                </h5>
                <div class="row g-3 mb-4">
                  <div class="col-md-6">
                    <label class="form-label">License Number</label>
                    <input type="text" class="form-control" formControlName="licenseNumber">
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Specialization <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" formControlName="specialization"
                           [class.is-invalid]="submitted && f['specialization'].errors">
                  </div>
                  <div class="col-md-4">
                    <label class="form-label">Years of Experience</label>
                    <input type="number" class="form-control" formControlName="yearsOfExperience">
                  </div>
                  <div class="col-md-4">
                    <label class="form-label">Consultation Fee</label>
                    <input type="number" class="form-control" formControlName="consultationFee">
                  </div>
                  <div class="col-md-4">
                    <label class="form-label">Status</label>
                    <select class="form-select" formControlName="status">
                      <option *ngFor="let status of statuses" [value]="status">{{ status }}</option>
                    </select>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Department</label>
                    <input type="text" class="form-control" formControlName="department">
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Room Number</label>
                    <input type="text" class="form-control" formControlName="roomNumber">
                  </div>
                  <div class="col-12">
                    <label class="form-label">Biography</label>
                    <textarea class="form-control" formControlName="biography" rows="3"></textarea>
                  </div>
                </div>

                <!-- Emergency Contact -->
                <h5 class="border-bottom pb-2 mb-3">
                  <i class="bi bi-telephone-plus me-2"></i>Emergency Contact
                </h5>
                <div class="row g-3 mb-4">
                  <div class="col-md-6">
                    <label class="form-label">Contact Name</label>
                    <input type="text" class="form-control" formControlName="emergencyContactName">
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Contact Phone</label>
                    <input type="tel" class="form-control" formControlName="emergencyContactPhone">
                  </div>
                </div>

                <div *ngIf="error" class="alert alert-danger" role="alert">
                  <i class="bi bi-exclamation-triangle-fill me-2"></i>{{ error }}
                </div>

                <div *ngIf="success" class="alert alert-success" role="alert">
                  <i class="bi bi-check-circle-fill me-2"></i>{{ success }}
                </div>

                <div class="d-flex gap-2">
                  <button type="submit" class="btn btn-primary" [disabled]="loading">
                    <span *ngIf="loading" class="spinner-border spinner-border-sm me-2"></span>
                    <i *ngIf="!loading" class="bi bi-check-circle me-2"></i>
                    {{ loading ? 'Saving...' : (isEditMode ? 'Update Doctor' : 'Add Doctor') }}
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
export class DoctorFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private doctorService = inject(DoctorService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  doctorForm: FormGroup;
  submitted = false;
  loading = false;
  error = '';
  success = '';
  isEditMode = false;
  doctorId: number | null = null;

  genders = Object.values(Gender);
  statuses = Object.values(DoctorStatus);

  constructor() {
    this.doctorForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      dateOfBirth: [''],
      gender: ['', Validators.required],
      licenseNumber: [''],
      specialization: ['', Validators.required],
      yearsOfExperience: [0],
      consultationFee: [0],
      biography: [''],
      department: [''],
      roomNumber: [''],
      status: [DoctorStatus.ACTIVE],
      emergencyContactName: [''],
      emergencyContactPhone: [''],
      userId: ['']
    });
  }

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.doctorForm.patchValue({ userId: currentUser.id });
    }

    this.doctorId = this.route.snapshot.params['id'];
    if (this.doctorId) {
      this.isEditMode = true;
      this.loadDoctor(this.doctorId);
    }
  }

  get f() {
    return this.doctorForm.controls;
  }

  loadDoctor(id: number): void {
    this.loading = true;
    this.doctorService.getDoctorById(id).subscribe({
      next: (doctor) => {
        this.doctorForm.patchValue(doctor);
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load doctor';
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';
    this.success = '';

    if (this.doctorForm.invalid) {
      return;
    }

    this.loading = true;

    if (this.isEditMode && this.doctorId) {
      this.doctorService.updateDoctor(this.doctorId, this.doctorForm.value).subscribe({
        next: () => {
          this.success = 'Doctor updated successfully!';
          setTimeout(() => this.router.navigate(['/doctors']), 2000);
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to update doctor';
          this.loading = false;
        }
      });
    } else {
      this.doctorService.createDoctor(this.doctorForm.value).subscribe({
        next: () => {
          this.success = 'Doctor added successfully!';
          setTimeout(() => this.router.navigate(['/doctors']), 2000);
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to add doctor';
          this.loading = false;
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/doctors']);
  }
}
