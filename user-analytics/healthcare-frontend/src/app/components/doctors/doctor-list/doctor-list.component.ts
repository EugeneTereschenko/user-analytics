import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { DoctorService } from '../../../services/doctor.service';
import { Doctor, DoctorStatus } from '../../../models/doctor.model';

@Component({
  selector: 'app-doctor-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div class="container-fluid py-4">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <button class="btn btn-outline-secondary btn-sm mb-2" routerLink="/dashboard">
            <i class="bi bi-arrow-left me-2"></i>Back to Dashboard
          </button>
          <h2 class="mb-1"><i class="bi bi-hospital me-2"></i>Doctors</h2>
          <p class="text-muted mb-0">Manage doctor profiles and information</p>
        </div>
        <button class="btn btn-primary" routerLink="/doctors/new">
          <i class="bi bi-plus-circle me-2"></i>Add Doctor
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
              <label class="form-label small text-muted">Specialization</label>
              <select class="form-select" [(ngModel)]="filterSpecialization" (change)="applyFilters()">
                <option value="">All Specializations</option>
                <option *ngFor="let spec of specializations" [value]="spec">{{ spec }}</option>
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

      <!-- Stats Cards -->
      <div class="row g-3 mb-4">
        <div class="col-md-3">
          <div class="card border-0 shadow-sm bg-primary text-white">
            <div class="card-body">
              <h6 class="text-uppercase mb-2 opacity-75">Total Doctors</h6>
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
              <h6 class="text-uppercase mb-2 opacity-75">Specializations</h6>
              <h3 class="mb-0">{{ specializations.length }}</h3>
            </div>
          </div>
        </div>
      </div>

      <!-- Doctors Grid -->
      <div *ngIf="loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status"></div>
        <p class="text-muted mt-3">Loading doctors...</p>
      </div>

      <div *ngIf="!loading && doctors.length === 0" class="text-center py-5">
        <i class="bi bi-person-x display-1 text-muted"></i>
        <h5 class="mt-3 text-muted">No doctors found</h5>
        <button class="btn btn-primary mt-3" routerLink="/doctors/new">
          <i class="bi bi-plus-circle me-2"></i>Add Doctor
        </button>
      </div>

      <div *ngIf="!loading && doctors.length > 0" class="row g-4 mb-4">
        <div class="col-md-6 col-xl-4" *ngFor="let doctor of doctors">
          <div class="card h-100 shadow-sm hover-card">
            <div class="card-body">
              <div class="d-flex align-items-start mb-3">
                <div class="flex-shrink-0">
                  <div class="avatar-circle bg-primary text-white">
                    {{ getInitials(doctor.firstName, doctor.lastName) }}
                  </div>
                </div>
                <div class="flex-grow-1 ms-3">
                  <h5 class="mb-1">Dr. {{ doctor.firstName }} {{ doctor.lastName }}</h5>
                  <p class="text-muted small mb-2">{{ doctor.specialization }}</p>
                  <span class="badge" [ngClass]="getStatusBadgeClass(doctor.status)">
                    {{ doctor.status }}
                  </span>
                </div>
              </div>

              <div class="mb-3">
                <div class="d-flex align-items-center mb-2">
                  <i class="bi bi-envelope text-muted me-2"></i>
                  <small>{{ doctor.email }}</small>
                </div>
                <div class="d-flex align-items-center mb-2">
                  <i class="bi bi-telephone text-muted me-2"></i>
                  <small>{{ doctor.phoneNumber }}</small>
                </div>
                <div class="d-flex align-items-center" *ngIf="doctor.department">
                  <i class="bi bi-building text-muted me-2"></i>
                  <small>{{ doctor.department }}</small>
                </div>
              </div>

              <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                <div>
                  <small class="text-muted">Experience</small>
                  <div class="fw-semibold">{{ doctor.yearsOfExperience || 0 }} years</div>
                </div>
                <div class="btn-group btn-group-sm">
                  <button class="btn btn-outline-primary" [routerLink]="['/doctors', doctor.id]" title="View">
                    <i class="bi bi-eye"></i>
                  </button>
                  <button class="btn btn-outline-info" [routerLink]="['/doctors', doctor.id, 'edit']" title="Edit">
                    <i class="bi bi-pencil"></i>
                  </button>
                  <button class="btn btn-outline-danger" (click)="deleteDoctor(doctor.id!)" title="Delete">
                    <i class="bi bi-trash"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
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
  `,
  styles: [`
    .hover-card {
      transition: transform 0.2s, box-shadow 0.2s;
    }
    .hover-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 20px rgba(0,0,0,0.1) !important;
    }
    .avatar-circle {
      width: 56px;
      height: 56px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 20px;
      font-weight: bold;
    }
  `]
})
export class DoctorListComponent implements OnInit {
  private doctorService = inject(DoctorService);
  private router = inject(Router);

  doctors: Doctor[] = [];
  specializations: string[] = [];
  loading = false;
  currentPage = 0;
  pageSize = 12;
  totalPages = 0;
  totalElements = 0;

  searchQuery = '';
  filterSpecialization = '';
  filterStatus = '';
  statuses = Object.values(DoctorStatus);

  activeCount = 0;
  onLeaveCount = 0;

  ngOnInit(): void {
    this.loadDoctors();
    this.loadSpecializations();
    this.loadActiveDoctors();
  }

  loadDoctors(): void {
    this.loading = true;
    this.doctorService.getAllDoctors(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.doctors = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.updateCounts();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading doctors:', error);
        this.loading = false;
      }
    });
  }

  loadSpecializations(): void {
    this.doctorService.getAllSpecializations().subscribe({
      next: (specs) => this.specializations = specs
    });
  }

  loadActiveDoctors(): void {
    this.doctorService.getActiveDoctors().subscribe({
      next: (doctors) => this.activeCount = doctors.length
    });
  }

  updateCounts(): void {
    this.onLeaveCount = this.doctors.filter(d => d.status === DoctorStatus.ON_LEAVE).length;
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.doctorService.searchDoctors(this.searchQuery, this.currentPage, this.pageSize).subscribe({
        next: (response) => {
          this.doctors = response.content;
          this.totalPages = response.totalPages;
        }
      });
    } else {
      this.loadDoctors();
    }
  }

  applyFilters(): void {
    if (this.filterSpecialization) {
      this.doctorService.getDoctorsBySpecialization(this.filterSpecialization, this.currentPage, this.pageSize).subscribe({
        next: (response) => {
          this.doctors = response.content;
          this.totalPages = response.totalPages;
        }
      });
    } else if (this.filterStatus) {
      this.doctorService.getDoctorsByStatus(this.filterStatus as DoctorStatus, this.currentPage, this.pageSize).subscribe({
        next: (response) => {
          this.doctors = response.content;
          this.totalPages = response.totalPages;
        }
      });
    } else {
      this.loadDoctors();
    }
  }

  clearFilters(): void {
    this.searchQuery = '';
    this.filterSpecialization = '';
    this.filterStatus = '';
    this.loadDoctors();
  }

  deleteDoctor(id: number): void {
    if (confirm('Are you sure you want to delete this doctor?')) {
      this.doctorService.deleteDoctor(id).subscribe({
        next: () => this.loadDoctors(),
        error: (error) => console.error('Error deleting doctor:', error)
      });
    }
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadDoctors();
    }
  }

  getPages(): number[] {
    return Array.from({ length: Math.min(this.totalPages, 5) }, (_, i) => i);
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
