import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MedicalRecordService } from '../../../services/medical-record.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-medical-record-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './medical-record-list.component.html'
})
export class MedicalRecordListComponent implements OnInit {
  records: any[] = [];

  constructor(private medicalRecordService: MedicalRecordService, private router: Router) {}

  ngOnInit(): void {
    this.loadRecords();
  }

  loadRecords(): void {
    this.medicalRecordService.getAll().subscribe({
      next: data => this.records = data.content,
      error: err => alert('Failed to load records')
    });
  }

  goToDetail(id: number) {
    this.router.navigate(['/medical-records', id]);
  }

  addRecord() {
    this.router.navigate(['/medical-records/new']);
  }
}
