import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { StaffService } from '../../../services/staff.service';
import { AuthService } from '../../../services/auth.service';
import { StaffRole, StaffStatus, Gender } from '../../../models/staff.model';

@Component({
  selector: 'app-staff-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="container py-4">
      <div class="row justify-content-center">
        <div class="col-lg-10">
          <button class="btn btn-outline-secondary btn-sm mb-3" routerLink="/dashboard">
            <i class="bi bi-arrow-left me-2"></i>Back to Dashboard
          </button>
          <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
              <h4 class="mb-0">
                <i class="bi bi-person-plus me-2"></i>
                {{ isEditMode ? 'Edit Staff' : 'Add New Staff' }}
              </h4>
            </div>
            <div class="card-body p-4">
              <form [formGroup]="staffForm" (ngSubmit)="onSubmit()">
                <!-- Personal Information -->
                <h5 class="border-bottom pb-2 mb-3">
                  <i class="bi bi-person me-2"></i>Personal Information
                </h5>
                <div class="row g-3 mb-4">
                  <div class="col-md-6">
                    <label class="form-label">First Name <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" formControlName="firstName"
                           [class.is-invalid]="submitted && f['firstName'].errors">
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Last Name <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" formControlName="lastName"
                           [class.is-invalid]="submitted && f['lastName'].errors">
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Email <span class="text-danger">*</span></label>
                    <input type="email" class="form-control" formControlName="email"
                           [class.is-invalid]="submitted && f['email'].errors">
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Phone <span class="text-danger">*</span></label>
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

                <!-- Employment Information -->
                <h5 class="border-bottom pb-2 mb-3">
                  <i class="bi bi-briefcase me-2"></i>Employment Information
                </h5>
                <div class="row g-3 mb-4">
                  <div class="col-md-6">
                    <label class="form-label">Employee ID</label>
                    <input type="text" class="form-control" formControlName="employeeId">
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Role <span class="text-danger">*</span></label>
                    <select class="form-select" formControlName="role"
                            [class.is-invalid]="submitted && f['role'].errors">
                      <option value="">Select role</option>
                      <option *ngFor="let role of roles" [value]="role">{{ formatRole(role) }}</option>
                    </select>
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Department <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" formControlName="department"
                           [class.is-invalid]="submitted && f['department'].errors">
                  </div>
                  <div class="col-md-6">
                    <label class="form-label">Shift</label>
                    <select class="form-select" formControlName="shift">
                      <option value="">Select shift</option>
                      <option value="MORNING">Morning (6 AM - 2 PM)</option>
                      <option value="AFTERNOON">Afternoon (2 PM - 10 PM)</option>
                      <option value="NIGHT">Night (10 PM - 6 AM)</option>
                      <option value="ROTATING">Rotating</option>
                    </select>
                  </div>
                  <div class="col-md-4">
                    <label class="form-label">Salary</label>
                    <input type="number" class="form-control" formControlName="salary">
                  </div>
                  <div class="col-md-4">
                    <label class="form-label">Joined Date</label>
                    <input type="date" class="form-control" formControlName="joinedDate">
                  </div>
                  <div class="col-md-4">
                    <label class="form-label">Status</label>
                    <select class="form-select" formControlName="status">
                      <option *ngFor="let status of statuses" [value]="status">{{ status }}</option>
                    </select>
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

                <div *ngIf="error" class="alert alert-danger">
                  <i class="bi bi-exclamation-triangle-fill me-2"></i>{{ error }}
                </div>

                <div *ngIf="success" class="alert alert-success">
                  <i class="bi bi-check-circle-fill me-2"></i>{{ success }}
                </div>

                <div class="d-flex gap-2">
                  <button type="submit" class="btn btn-primary" [disabled]="loading">
                    <span *ngIf="loading" class="spinner-border spinner-border-sm me-2"></span>
                    {{ loading ? 'Saving...' : (isEditMode ? 'Update Staff' : 'Add Staff') }}
                  </button>
                  <button type="button" class="btn btn-outline-secondary" (click)="cancel()">
                    Cancel
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
export class StaffFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private staffService = inject(StaffService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  staffForm: FormGroup;
  submitted = false;
  loading = false;
  error = '';
  success = '';
  isEditMode = false;
  staffId: number | null = null;

  genders = Object.values(Gender);
  roles = Object.values(StaffRole);
  statuses = Object.values(StaffStatus);

  constructor() {
    this.staffForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      dateOfBirth: [''],
      gender: ['', Validators.required],
      employeeId: [''],
      role: ['', Validators.required],
      department: ['', Validators.required],
      shift: [''],
      salary: [0],
      joinedDate: [''],
      status: [StaffStatus.ACTIVE],
      emergencyContactName: [''],
      emergencyContactPhone: [''],
      userId: ['']
    });
  }

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.staffForm.patchValue({ userId: currentUser.id });
    }

    this.staffId = this.route.snapshot.params['id'];
    if (this.staffId) {
      this.isEditMode = true;
      this.loadStaff(this.staffId);
    }
  }

  get f() {
    return this.staffForm.controls;
  }

  loadStaff(id: number): void {
    this.loading = true;
    this.staffService.getStaffById(id).subscribe({
      next: (staff) => {
        this.staffForm.patchValue(staff);
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load staff';
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';
    this.success = '';

    if (this.staffForm.invalid) {
      return;
    }

    this.loading = true;

    if (this.isEditMode && this.staffId) {
      this.staffService.updateStaff(this.staffId, this.staffForm.value).subscribe({
        next: () => {
          this.success = 'Staff updated successfully!';
          setTimeout(() => this.router.navigate(['/staff']), 2000);
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to update staff';
          this.loading = false;
        }
      });
    } else {
      this.staffService.createStaff(this.staffForm.value).subscribe({
        next: () => {
          this.success = 'Staff added successfully!';
          setTimeout(() => this.router.navigate(['/staff']), 2000);
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to add staff';
          this.loading = false;
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/staff']);
  }

  formatRole(role: string): string {
    return role.replace(/_/g, ' ').toLowerCase()
      .replace(/\b\w/g, c => c.toUpperCase());
  }
}
