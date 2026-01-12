import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PatientService, PageResponse } from '../../../services/patient.service';
import { Patient, PatientStatus } from '../../../models/patient.model';

@Component({
  selector: 'app-patient-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './patient-list.component.html',
  styleUrls: ['./patient-list.component.css']
})
export class PatientListComponent implements OnInit {
  patients: Patient[] = [];
  searchQuery: string = '';
  filterStatus: string = 'ALL';
  currentPage: number = 0;
  totalPages: number = 1;
  totalElements: number = 0;
  pageSize: number = 10;
  isLoading: boolean = false;

  PatientStatus = PatientStatus;
  Math = Math;

  constructor(
    private patientService: PatientService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadPatients();
  }

  loadPatients(): void {
    this.isLoading = true;
    const status = this.filterStatus !== 'ALL' ? this.filterStatus as PatientStatus : undefined;
    
    this.patientService.getPatients(
      this.currentPage,
      this.pageSize,
      this.searchQuery || undefined,
      status
    ).subscribe({
      next: (response: PageResponse<Patient>) => {
        this.patients = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading patients:', error);
        this.isLoading = false;
      }
    });
  }

  onSearch(): void {
    this.currentPage = 0;
    this.loadPatients();
  }

  onFilterChange(): void {
    this.currentPage = 0;
    this.loadPatients();
  }

  viewPatient(id: number): void {
    this.router.navigate(['/patients', id]);
  }

  editPatient(id: number): void {
    this.router.navigate(['/patients', id, 'edit']);
  }

  createPatient(): void {
    this.router.navigate(['/patients', 'new']);
  }

  deactivatePatient(id: number): void {
    if (confirm('Are you sure you want to deactivate this patient?')) {
      this.patientService.deactivatePatient(id).subscribe({
        next: () => {
          this.loadPatients();
        },
        error: (error) => {
          console.error('Error deactivating patient:', error);
          alert('Failed to deactivate patient');
        }
      });
    }
  }

  deletePatient(id: number): void {
    if (confirm('Are you sure you want to delete this patient? This action cannot be undone.')) {
      this.patientService.deletePatient(id).subscribe({
        next: () => {
          this.loadPatients();
        },
        error: (error) => {
          console.error('Error deleting patient:', error);
          alert('Failed to delete patient');
        }
      });
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadPatients();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadPatients();
    }
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadPatients();
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxPagesToShow = 5;
    let startPage = Math.max(0, this.currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(this.totalPages - 1, startPage + maxPagesToShow - 1);

    if (endPage - startPage < maxPagesToShow - 1) {
      startPage = Math.max(0, endPage - maxPagesToShow + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }

    return pages;
  }

  getStatusClass(status: PatientStatus): string {
    switch (status) {
      case PatientStatus.ACTIVE:
        return 'badge bg-success';
      case PatientStatus.INACTIVE:
        return 'badge bg-warning text-dark';
      case PatientStatus.DECEASED:
        return 'badge bg-secondary';
      default:
        return 'badge bg-secondary';
    }
  }

  getInitials(firstName: string, lastName: string): string {
    return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString();
  }
}
