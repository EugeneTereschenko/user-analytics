import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MedicalRecordService } from '../../../services/medical-record.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-medical-record-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './medical-record-detail.component.html'
})
export class MedicalRecordDetailComponent implements OnInit {
  record: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private medicalRecordService: MedicalRecordService
  ) {}

  ngOnInit(): void {
    const recordId = Number(this.route.snapshot.paramMap.get('id'));
    this.medicalRecordService.getById(recordId).subscribe({
      next: data => this.record = data,
      error: err => alert('Record not found')
    });
  }

  editRecord() {
    this.router.navigate(['/medical-records', this.record.id, 'edit']);
  }

  deleteRecord() {
    if (!confirm('Are you sure?')) return;
    this.medicalRecordService.delete(this.record.id).subscribe({
      next: () => {
        alert('Deleted!');
        this.router.navigate(['/medical-records']);
      },
      error: err => alert('Delete failed')
    });
  }

  goBack() {
    this.router.navigate(['/medical-records']);
  }
}
