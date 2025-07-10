import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { AuditService } from '../../audit-service.service'; // Adjust the path as necessary

import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-audit-logs',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './audit-logs.component.html',
  styleUrl: './audit-logs.component.css'
})
export class AuditLogsComponent implements OnInit {
  logs: any[] = [];

  constructor(private auditService: AuditService) {}

  ngOnInit() {
    this.auditService.getLogs().subscribe(data => this.logs = data);
  }
}

