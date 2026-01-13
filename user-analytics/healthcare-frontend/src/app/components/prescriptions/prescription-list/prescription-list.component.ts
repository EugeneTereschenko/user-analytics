import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { PrescriptionService } from '../../../services/prescription.service';
import { Prescription, PrescriptionStatus } from '../../../models/prescription.model';

@Component({
  selector: 'app-prescription-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './prescription-list.component.html',
  styleUrls: ['./prescription-list.component.css']
})
export class PrescriptionListComponent implements OnInit {
  prescriptions: Prescription[] = [];
  filteredPrescriptions: Prescription[] = [];
  selectedPrescription: Prescription | null = null;
  
  // Pagination
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  
  // Filters
  searchQuery = '';
  statusFilter: PrescriptionStatus | 'ALL' = 'ALL';
  patientIdFilter = '';
  doctorIdFilter = '';
  
  // Loading state
  isLoading = false;
  error: string | null = null;
  
  // Enums for template
  prescriptionStatuses = Object.values(PrescriptionStatus);
  
  // For template use
  Math = Math;
  
  constructor(private prescriptionService: PrescriptionService) { }

  ngOnInit(): void {
    this.loadPrescriptions();
  }

  loadPrescriptions(): void {
    this.isLoading = true;
    this.error = null;
    
    this.prescriptionService.getAllPrescriptions(this.currentPage, this.pageSize)
      .subscribe({
        next: (data) => {
          this.prescriptions = data.content;
          this.filteredPrescriptions = data.content;
          this.totalElements = data.totalElements;
          this.totalPages = data.totalPages;
          this.isLoading = false;
        },
        error: (err) => {
          this.error = 'Failed to load prescriptions';
          console.error(err);
          this.isLoading = false;
        }
      });
  }

  searchPrescriptions(): void {
    if (this.searchQuery.trim()) {
      this.isLoading = true;
      this.prescriptionService.searchPrescriptions(this.searchQuery, this.currentPage, this.pageSize)
        .subscribe({
          next: (data) => {
            this.filteredPrescriptions = data.content;
            this.totalElements = data.totalElements;
            this.totalPages = data.totalPages;
            this.isLoading = false;
          },
          error: (err) => {
            this.error = 'Search failed';
            console.error(err);
            this.isLoading = false;
          }
        });
    } else {
      this.loadPrescriptions();
    }
  }

  filterByStatus(): void {
    if (this.statusFilter !== 'ALL') {
      this.isLoading = true;
      this.prescriptionService.getPrescriptionsByStatus(this.statusFilter as PrescriptionStatus, this.currentPage, this.pageSize)
        .subscribe({
          next: (data) => {
            this.filteredPrescriptions = data.content;
            this.totalElements = data.totalElements;
            this.totalPages = data.totalPages;
            this.isLoading = false;
          },
          error: (err) => {
            this.error = 'Filter failed';
            console.error(err);
            this.isLoading = false;
          }
        });
    } else {
      this.loadPrescriptions();
    }
  }

  filterByPatient(): void {
    if (this.patientIdFilter) {
      this.isLoading = true;
      this.prescriptionService.getPrescriptionsByPatient(+this.patientIdFilter, this.currentPage, this.pageSize)
        .subscribe({
          next: (data) => {
            this.filteredPrescriptions = data.content;
            this.totalElements = data.totalElements;
            this.totalPages = data.totalPages;
            this.isLoading = false;
          },
          error: (err) => {
            this.error = 'Filter failed';
            console.error(err);
            this.isLoading = false;
          }
        });
    } else {
      this.loadPrescriptions();
    }
  }

  viewPrescription(prescription: Prescription): void {
    this.selectedPrescription = prescription;
  }

  dispensePrescription(id: number): void {
    const dispensedBy = prompt('Enter dispenser name:');
    if (dispensedBy) {
      this.prescriptionService.dispensePrescription(id, dispensedBy)
        .subscribe({
          next: () => {
            this.loadPrescriptions();
            alert('Prescription dispensed successfully');
          },
          error: (err) => {
            alert('Failed to dispense prescription');
            console.error(err);
          }
        });
    }
  }

  refillPrescription(id: number): void {
    if (confirm('Are you sure you want to refill this prescription?')) {
      this.prescriptionService.refillPrescription(id)
        .subscribe({
          next: () => {
            this.loadPrescriptions();
            alert('Prescription refilled successfully');
          },
          error: (err) => {
            alert('Failed to refill prescription');
            console.error(err);
          }
        });
    }
  }

  cancelPrescription(id: number): void {
    const reason = prompt('Enter cancellation reason:');
    if (reason !== null) {
      this.prescriptionService.cancelPrescription(id, reason)
        .subscribe({
          next: () => {
            this.loadPrescriptions();
            alert('Prescription cancelled successfully');
          },
          error: (err) => {
            alert('Failed to cancel prescription');
            console.error(err);
          }
        });
    }
  }

  deletePrescription(id: number): void {
    if (confirm('Are you sure you want to delete this prescription? This action cannot be undone.')) {
      this.prescriptionService.deletePrescription(id)
        .subscribe({
          next: () => {
            this.loadPrescriptions();
            alert('Prescription deleted successfully');
          },
          error: (err) => {
            alert('Failed to delete prescription');
            console.error(err);
          }
        });
    }
  }

  isExpired(validUntil?: string): boolean {
    if (!validUntil) return false;
    return new Date(validUntil) < new Date();
  }

  getStatusClass(status: PrescriptionStatus): string {
    const statusClasses: Record<PrescriptionStatus, string> = {
      [PrescriptionStatus.ACTIVE]: 'badge bg-success',
      [PrescriptionStatus.DISPENSED]: 'badge bg-info',
      [PrescriptionStatus.COMPLETED]: 'badge bg-secondary',
      [PrescriptionStatus.CANCELLED]: 'badge bg-danger',
      [PrescriptionStatus.EXPIRED]: 'badge bg-warning text-dark',
      [PrescriptionStatus.ON_HOLD]: 'badge bg-warning'
    };
    return statusClasses[status];
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadPrescriptions();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadPrescriptions();
    }
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadPrescriptions();
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }
}
