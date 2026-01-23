import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PatientService } from '../../../services/patient.service';
import { Patient, PatientStatus } from '../../../models/patient.model';

@Component({
  selector: 'app-patient-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.css']
})
export class PatientDetailComponent implements OnInit {
  patient: Patient | null = null;
  isLoading: boolean = false;
  patientId!: number;

  PatientStatus = PatientStatus;

  constructor(
    private patientService: PatientService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.patientId = +params['id'];
      this.loadPatient();
    });
  }

  loadPatient(): void {
    this.isLoading = true;
    this.patientService.getPatientById(this.patientId).subscribe({
      next: (patient: Patient) => {
        this.patient = patient;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading patient:', error);
        alert('Failed to load patient data');
        this.isLoading = false;
        this.router.navigate(['/patients']);
      }
    });
  }

  editPatient(): void {
    this.router.navigate(['/patients', this.patientId, 'edit']);
  }

  deactivatePatient(): void {
    if (confirm('Are you sure you want to deactivate this patient?')) {
      this.patientService.deactivatePatient(this.patientId).subscribe({
        next: () => {
          this.loadPatient();
        },
        error: (error) => {
          console.error('Error deactivating patient:', error);
          alert('Failed to deactivate patient');
        }
      });
    }
  }

  deletePatient(): void {
    if (confirm('Are you sure you want to delete this patient? This action cannot be undone.')) {
      this.patientService.deletePatient(this.patientId).subscribe({
        next: () => {
          this.router.navigate(['/patients']);
        },
        error: (error) => {
          console.error('Error deleting patient:', error);
          alert('Failed to delete patient');
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/patients']);
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

  calculateAge(dateOfBirth: string): number {
    const today = new Date();
    const birthDate = new Date(dateOfBirth);
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    
    return age;
  }
}
