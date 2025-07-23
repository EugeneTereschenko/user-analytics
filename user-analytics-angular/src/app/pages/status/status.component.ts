import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SystemstatusService } from '../../services/systemstatus.service';

@Component({
  selector: 'app-status',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './status.component.html',
})
export class StatusComponent implements OnInit {
  status: any;

  constructor(private systemstatusService: SystemstatusService) {}

  ngOnInit(): void {
    this.systemstatusService.getSystemStatus().subscribe((status) => {
      this.status = status;
    });
  }
}

