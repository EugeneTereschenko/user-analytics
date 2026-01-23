import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PatientService } from '../../../services/patient.service';
import { Patient, Gender, PatientStatus } from '../../../models/patient.model';

@Component({
  selector: 'app-patient-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './patient-form.component.html',
  styleUrls: ['./patient-form.component.css']
})
export class PatientFormComponent implements OnInit {
  patientForm!: FormGroup;
  isEditMode: boolean = false;
  patientId?: number;
  isLoading: boolean = false;
  isSubmitting: boolean = false;

  Gender = Gender;
  PatientStatus = PatientStatus;

  constructor(
    private fb: FormBuilder,
    private patientService: PatientService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id'] && params['id'] !== 'new') {
        this.patientId = +params['id'];
        this.isEditMode = true;
        this.loadPatient();
      }
    });
  }

  initializeForm(): void {
    this.patientForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      gender: [Gender.MALE, Validators.required],
      bloodGroup: [''],
      status: [PatientStatus.ACTIVE, Validators.required],
      address: this.fb.group({
        street: [''],
        city: [''],
        state: [''],
        zipCode: [''],
        country: ['']
      }),
      emergencyContacts: this.fb.array([]),
      allergies: this.fb.array([]),
      medicalNotes: ['']
    });
  }

  get emergencyContacts(): FormArray {
    return this.patientForm.get('emergencyContacts') as FormArray;
  }

  get allergies(): FormArray {
    return this.patientForm.get('allergies') as FormArray;
  }

  loadPatient(): void {
    if (!this.patientId) return;

    this.isLoading = true;
    this.patientService.getPatientById(this.patientId).subscribe({
      next: (patient: Patient) => {
        this.patchFormWithPatient(patient);
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

  patchFormWithPatient(patient: Patient): void {
    this.patientForm.patchValue({
      firstName: patient.firstName,
      lastName: patient.lastName,
      email: patient.email,
      phoneNumber: patient.phoneNumber,
      dateOfBirth: patient.dateOfBirth,
      gender: patient.gender,
      bloodGroup: patient.bloodGroup || '',
      status: patient.status,
      address: patient.address,
      medicalNotes: patient.medicalNotes || ''
    });

    // Add emergency contacts
    patient.emergencyContacts.forEach(contact => {
      this.emergencyContacts.push(this.fb.group({
        name: [contact.name],
        relationship: [contact.relationship],
        phoneNumber: [contact.phoneNumber],
        email: [contact.email]
      }));
    });

    // Add allergies
    patient.allergies.forEach(allergy => {
      this.allergies.push(this.fb.control(allergy));
    });
  }

  addEmergencyContact(): void {
    this.emergencyContacts.push(this.fb.group({
      name: [''],
      relationship: [''],
      phoneNumber: [''],
      email: ['']
    }));
  }

  removeEmergencyContact(index: number): void {
    this.emergencyContacts.removeAt(index);
  }

  addAllergy(): void {
    const allergy = prompt('Enter allergy:');
    if (allergy && allergy.trim()) {
      this.allergies.push(this.fb.control(allergy.trim()));
    }
  }

  removeAllergy(index: number): void {
    this.allergies.removeAt(index);
  }

  onSubmit(): void {
    if (this.patientForm.invalid) {
      this.patientForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    const patientData: Patient = {
      ...this.patientForm.value,
      emergencyContacts: this.emergencyContacts.value,
      allergies: this.allergies.value
    };

    const operation = this.isEditMode && this.patientId
      ? this.patientService.updatePatient(this.patientId, patientData)
      : this.patientService.createPatient(patientData);

    operation.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.router.navigate(['/patients']);
      },
      error: (error) => {
        console.error('Error saving patient:', error);
        alert('Failed to save patient');
        this.isSubmitting = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/patients']);
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.patientForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }
}
