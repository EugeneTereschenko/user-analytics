import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { StaffService } from '../../../services/staff.service';
import { Staff } from '../../../models/staff.model';

@Component({
  selector: 'app-staff-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="container py-4">
      <div class="row justify-content-center">
        <div class="col-lg-10">
          <div class="d-flex justify-content-between mb-4">
            <h2><i class="bi bi-person-badge me-2"></i>Staff Profile</h2>
            <button class="btn btn-outline-secondary" routerLink="/staff">
              <i class="bi bi-arrow-left me-2"></i>Back
            </button>
          </div>

          <div *ngIf="loading" class="text-center py-5">
            <div class="spinner-border text-primary"></div>
          </div>

          <div *ngIf="!loading && staff" class="row g-4">
            <div class="col-md-4">
              <div class="card shadow-sm text-center">
                <div class="card-body p-4">
                  <div class="avatar-circle-lg bg-info text-white mx-auto mb-3">
                    {{ getInitials(staff.firstName, staff.lastName) }}
                  </div>
                  <h4>{{ staff.firstName }} {{ staff.lastName }}</h4>
                  <p class="text-muted">{{ formatRole(staff.role) }}</p>
                  <span class="badge fs-6" [ngClass]="getStatusBadgeClass(staff.status)">
                    {{ staff.status }}
                  </span>

                  <div class="d-grid gap-2 mt-4">
                    <button class="btn btn-primary" [routerLink]="['/staff', staff.id, 'edit']">
                      <i class="bi bi-pencil me-2"></i>Edit Profile
                    </button>
                    <button class="btn btn-outline-danger" (click)="deleteStaff()">
                      <i class="bi bi-trash me-2"></i>Delete
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-md-8">
              <div class="card shadow-sm mb-3">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-person-lines-fill me-2"></i>Contact Information</h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="text-muted small">Email</label>
                      <p class="fw-semibold mb-0">{{ staff.email }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Phone</label>
                      <p class="fw-semibold mb-0">{{ staff.phoneNumber }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Date of Birth</label>
                      <p class="fw-semibold mb-0">{{ staff.dateOfBirth || 'N/A' }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Gender</label>
                      <p class="fw-semibold mb-0">{{ staff.gender }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card shadow-sm mb-3">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-briefcase me-2"></i>Employment Information</h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="text-muted small">Employee ID</label>
                      <p class="fw-semibold mb-0">{{ staff.employeeId || 'N/A' }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Department</label>
                      <p class="fw-semibold mb-0">{{ staff.department }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Shift</label>
                      <p class="fw-semibold mb-0">{{ staff.shift || 'N/A' }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Joined Date</label>
                      <p class="fw-semibold mb-0">{{ staff.joinedDate || 'N/A' }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card shadow-sm" *ngIf="staff.emergencyContactName">
                <div class="card-header bg-light">
                  <h5 class="mb-0"><i class="bi bi-telephone-plus me-2"></i>Emergency Contact</h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="text-muted small">Name</label>
                      <p class="fw-semibold mb-0">{{ staff.emergencyContactName }}</p>
                    </div>
                    <div class="col-md-6">
                      <label class="text-muted small">Phone</label>
                      <p class="fw-semibold mb-0">{{ staff.emergencyContactPhone }}</p>
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
export class StaffDetailComponent implements OnInit {
  private staffService = inject(StaffService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  staff: Staff | null = null;
  loading = false;

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.loadStaff(id);
    }
  }

  loadStaff(id: number): void {
    this.loading = true;
    this.staffService.getStaffById(id).subscribe({
      next: (staff) => {
        this.staff = staff;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  deleteStaff(): void {
    if (this.staff?.id && confirm('Delete this staff member?')) {
      this.staffService.deleteStaff(this.staff.id).subscribe({
        next: () => this.router.navigate(['/staff'])
      });
    }
  }

  getInitials(firstName: string, lastName: string): string {
    return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
  }

  formatRole(role: string): string {
    return role.replace(/_/g, ' ').toLowerCase()
      .replace(/\b\w/g, c => c.toUpperCase());
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
