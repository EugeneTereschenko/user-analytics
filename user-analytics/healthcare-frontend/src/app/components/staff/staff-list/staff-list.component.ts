import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { StaffService } from '../../../services/staff.service';
import { Staff, StaffRole, StaffStatus } from '../../../models/staff.model';

@Component({
  selector: 'app-staff-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div class="container-fluid py-4">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <button class="btn btn-outline-secondary btn-sm mb-2" routerLink="/dashboard">
            <i class="bi bi-arrow-left me-2"></i>Back to Dashboard
          </button>
          <h2 class="mb-1"><i class="bi bi-people me-2"></i>Staff Members</h2>
          <p class="text-muted mb-0">Manage staff members and assignments</p>
        </div>
        <button class="btn btn-primary" routerLink="/staff/new">
          <i class="bi bi-plus-circle me-2"></i>Add Staff
        </button>
      </div>

      <!-- Search and Filters -->
      <div class="card mb-4">
        <div class="card-body">
          <div class="row g-3">
            <div class="col-md-4">
              <label class="form-label small text-muted">Search</label>
              <input type="text" class="form-control" placeholder="Search by name, email..." 
                     [(ngModel)]="searchQuery" (input)="onSearch()">
            </div>
            <div class="col-md-3">
              <label class="form-label small text-muted">Role</label>
              <select class="form-select" [(ngModel)]="filterRole" (change)="applyFilters()">
                <option value="">All Roles</option>
                <option *ngFor="let role of roles" [value]="role">{{ formatRole(role) }}</option>
              </select>
            </div>
            <div class="col-md-2">
              <label class="form-label small text-muted">Status</label>
              <select class="form-select" [(ngModel)]="filterStatus" (change)="applyFilters()">
                <option value="">All Statuses</option>
                <option *ngFor="let status of statuses" [value]="status">{{ status }}</option>
              </select>
            </div>
            <div class="col-md-3 d-flex align-items-end">
              <button class="btn btn-outline-secondary w-100" (click)="clearFilters()">
                <i class="bi bi-x-circle me-2"></i>Clear Filters
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Stats -->
      <div class="row g-3 mb-4">
        <div class="col-md-3">
          <div class="card border-0 shadow-sm bg-primary text-white">
            <div class="card-body">
              <h6 class="text-uppercase mb-2 opacity-75">Total Staff</h6>
              <h3 class="mb-0">{{ totalElements }}</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm bg-success text-white">
            <div class="card-body">
              <h6 class="text-uppercase mb-2 opacity-75">Active</h6>
              <h3 class="mb-0">{{ activeCount }}</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm bg-warning text-white">
            <div class="card-body">
              <h6 class="text-uppercase mb-2 opacity-75">On Leave</h6>
              <h3 class="mb-0">{{ onLeaveCount }}</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card border-0 shadow-sm bg-info text-white">
            <div class="card-body">
              <h6 class="text-uppercase mb-2 opacity-75">Roles</h6>
              <h3 class="mb-0">{{ roles.length }}</h3>
            </div>
          </div>
        </div>
      </div>

      <!-- Staff Table -->
      <div class="card shadow-sm">
        <div class="card-body">
          <div *ngIf="loading" class="text-center py-5">
            <div class="spinner-border text-primary"></div>
            <p class="text-muted mt-3">Loading staff...</p>
          </div>

          <div *ngIf="!loading && staff.length === 0" class="text-center py-5">
            <i class="bi bi-person-x display-1 text-muted"></i>
            <h5 class="mt-3 text-muted">No staff members found</h5>
            <button class="btn btn-primary mt-3" routerLink="/staff/new">
              <i class="bi bi-plus-circle me-2"></i>Add Staff
            </button>
          </div>

          <div *ngIf="!loading && staff.length > 0" class="table-responsive">
            <table class="table table-hover align-middle">
              <thead class="table-light">
                <tr>
                  <th>Name</th>
                  <th>Role</th>
                  <th>Department</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Status</th>
                  <th class="text-center">Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let member of staff">
                  <td>
                    <div class="d-flex align-items-center">
                      <div class="avatar-circle-sm bg-info text-white me-2">
                        {{ getInitials(member.firstName, member.lastName) }}
                      </div>
                      <div>
                        <strong>{{ member.firstName }} {{ member.lastName }}</strong>
                        <br><small class="text-muted">{{ member.employeeId }}</small>
                      </div>
                    </div>
                  </td>
                  <td><span class="badge bg-primary">{{ formatRole(member.role) }}</span></td>
                  <td>{{ member.department }}</td>
                  <td><small>{{ member.email }}</small></td>
                  <td><small>{{ member.phoneNumber }}</small></td>
                  <td>
                    <span class="badge" [ngClass]="getStatusBadgeClass(member.status)">
                      {{ member.status }}
                    </span>
                  </td>
                  <td class="text-center">
                    <div class="btn-group btn-group-sm">
                      <button class="btn btn-outline-primary" [routerLink]="['/staff', member.id]" title="View">
                        <i class="bi bi-eye"></i>
                      </button>
                      <button class="btn btn-outline-info" [routerLink]="['/staff', member.id, 'edit']" title="Edit">
                        <i class="bi bi-pencil"></i>
                      </button>
                      <button class="btn btn-outline-danger" (click)="deleteStaff(member.id!)" title="Delete">
                        <i class="bi bi-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Pagination -->
          <nav *ngIf="totalPages > 1" class="mt-4">
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
  `,
  styles: [`
    .avatar-circle-sm {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14px;
      font-weight: bold;
    }
    .table th {
      font-size: 0.875rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
  `]
})
export class StaffListComponent implements OnInit {
  private staffService = inject(StaffService);
  private router = inject(Router);

  staff: Staff[] = [];
  loading = false;
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;

  searchQuery = '';
  filterRole = '';
  filterStatus = '';
  roles = Object.values(StaffRole);
  statuses = Object.values(StaffStatus);

  activeCount = 0;
  onLeaveCount = 0;

  ngOnInit(): void {
    this.loadStaff();
    this.loadActiveStaff();
  }

  loadStaff(): void {
    this.loading = true;
    this.staffService.getAllStaff(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.staff = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.updateCounts();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading staff:', error);
        this.loading = false;
      }
    });
  }

  loadActiveStaff(): void {
    this.staffService.getActiveStaff().subscribe({
      next: (staff) => this.activeCount = staff.length
    });
  }

  updateCounts(): void {
    this.onLeaveCount = this.staff.filter(s => s.status === StaffStatus.ON_LEAVE).length;
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.staffService.searchStaff(this.searchQuery, this.currentPage, this.pageSize).subscribe({
        next: (response) => {
          this.staff = response.content;
          this.totalPages = response.totalPages;
        }
      });
    } else {
      this.loadStaff();
    }
  }

  applyFilters(): void {
    if (this.filterRole) {
      this.staffService.getStaffByRole(this.filterRole as StaffRole, this.currentPage, this.pageSize).subscribe({
        next: (response) => {
          this.staff = response.content;
          this.totalPages = response.totalPages;
        }
      });
    } else if (this.filterStatus) {
      this.staffService.getStaffByStatus(this.filterStatus as StaffStatus, this.currentPage, this.pageSize).subscribe({
        next: (response) => {
          this.staff = response.content;
          this.totalPages = response.totalPages;
        }
      });
    } else {
      this.loadStaff();
    }
  }

  clearFilters(): void {
    this.searchQuery = '';
    this.filterRole = '';
    this.filterStatus = '';
    this.loadStaff();
  }

  deleteStaff(id: number): void {
    if (confirm('Are you sure you want to delete this staff member?')) {
      this.staffService.deleteStaff(id).subscribe({
        next: () => this.loadStaff(),
        error: (error) => console.error('Error deleting staff:', error)
      });
    }
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadStaff();
    }
  }

  getPages(): number[] {
    return Array.from({ length: Math.min(this.totalPages, 5) }, (_, i) => i);
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
