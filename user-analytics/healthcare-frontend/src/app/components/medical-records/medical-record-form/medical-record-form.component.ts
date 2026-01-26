import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MedicalRecordService } from '../../../services/medical-record.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-medical-record-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './medical-record-form.component.html'
})
export class MedicalRecordFormComponent implements OnInit {
  recordForm: FormGroup;
  isEdit = false;
  recordId: number = 0;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private medicalRecordService: MedicalRecordService
  ) {
    this.recordForm = this.fb.group({
      patientId: ['', Validators.required],
      doctorId: ['', Validators.required],
      recordDate: ['', Validators.required],
      recordType: ['', Validators.required],
      title: [''],
      diagnosis: [''],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.recordId = Number(this.route.snapshot.paramMap.get('id'));
    this.isEdit = !!this.recordId;

    if (this.isEdit) {
      this.medicalRecordService.getById(this.recordId).subscribe({
        next: (rec) => this.recordForm.patchValue(rec),
        error: () => alert('Failed to load for edit')
      });
    }
  }

  onSubmit() {
    if (this.isEdit) {
      this.medicalRecordService.update(this.recordId, this.recordForm.value).subscribe({
        next: () => {
          alert('Record updated!');
          this.router.navigate(['/medical-records', this.recordId]);
        },
        error: () => alert('Update failed')
      });
    } else {
      this.medicalRecordService.create(this.recordForm.value).subscribe({
        next: (data) => {
          alert('Created!');
          this.router.navigate(['/medical-records', data.id]);
        },
        error: () => alert('Create failed')
      });
    }
  }

  cancel() {
    if (this.isEdit) {
      this.router.navigate(['/medical-records', this.recordId]);
    } else {
      this.router.navigate(['/medical-records']);
    }
  }
}
