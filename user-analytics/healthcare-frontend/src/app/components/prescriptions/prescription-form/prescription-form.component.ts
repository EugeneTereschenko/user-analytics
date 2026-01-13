import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PrescriptionService } from '../../../services/prescription.service';
import { Prescription, PrescriptionStatus, Route, Medication } from '../../../models/prescription.model';

@Component({
  selector: 'app-prescription-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './prescription-form.component.html',
  styleUrls: ['./prescription-form.component.css']
})
export class PrescriptionFormComponent implements OnInit {
  prescription: Prescription = {
    patientId: 0,
    doctorId: 0,
    prescriptionDate: new Date().toISOString().split('T')[0],
    status: PrescriptionStatus.ACTIVE,
    medications: [],
    isRefillable: true,
    refillsAllowed: 0,
    refillsRemaining: 0
  };

  isEditMode = false;
  isLoading = false;
  error: string | null = null;
  
  // Enums for template
  prescriptionStatuses = Object.values(PrescriptionStatus);
  routes = Object.values(Route);

  constructor(
    private prescriptionService: PrescriptionService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loadPrescription(+id);
    }
  }

  loadPrescription(id: number): void {
    this.isLoading = true;
    this.prescriptionService.getPrescriptionById(id)
      .subscribe({
        next: (data) => {
          this.prescription = data;
          this.isLoading = false;
        },
        error: (err) => {
          this.error = 'Failed to load prescription';
          console.error(err);
          this.isLoading = false;
        }
      });
  }

  addMedication(): void {
    const newMed: Medication = {
      medicationName: '',
      dosage: '',
      frequency: '',
      route: Route.ORAL,
      isGenericAllowed: true,
      isControlledSubstance: false,
      priority: 1
    };
    this.prescription.medications.push(newMed);
  }

  removeMedication(index: number): void {
    this.prescription.medications.splice(index, 1);
  }

  onSubmit(): void {
    this.isLoading = true;
    this.error = null;

    const operation = this.isEditMode
      ? this.prescriptionService.updatePrescription(this.prescription.id!, this.prescription)
      : this.prescriptionService.createPrescription(this.prescription);

    operation.subscribe({
      next: () => {
        this.router.navigate(['/prescriptions']);
      },
      error: (err) => {
        this.error = this.isEditMode ? 'Failed to update prescription' : 'Failed to create prescription';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/prescriptions']);
  }
}
