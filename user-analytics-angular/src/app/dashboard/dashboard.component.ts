import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SummaryCardsComponent } from '../summary-cards/summary-cards.component';
import { LineChartComponent } from '../line-chart/line-chart.component';
import { PieChartComponent } from '../pie-chart/pie-chart.component';           // <-- adjust path as needed
import { AnalyticsService } from '../analytics.service';
import { NgChartsModule } from 'ng2-charts';
import { FeatureToggleService } from '../feature-toggle.service';
import { FormsModule } from '@angular/forms';

interface SummaryApiResponse {
  totalUsers: number;
  activeUsers: number;
  newUsersToday?: number;
  bounceRate?: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    SummaryCardsComponent,
    LineChartComponent,
    PieChartComponent,
    NgChartsModule,
    FormsModule,
    CommonModule
  ],

  template: `
    <h2>Analytics Dashboard</h2>

<app-summary-cards [summary]="summary"></app-summary-cards>

<ng-container *ngIf="featureService.current['featureNewChart']">
  <app-line-chart [data]="signupData"></app-line-chart>
</ng-container>

<app-pie-chart [data]="deviceData" title="Device Usage"></app-pie-chart>

<!-- Above your charts -->
<div class="row mb-3">
  <div class="col">
    <label>From:</label>
    <input type="date" class="form-control" [(ngModel)]="startDate" />
  </div>
  <div class="col">
    <label>To:</label>
    <input type="date" class="form-control" [(ngModel)]="endDate" />
  </div>
  <div class="col align-self-end">
    <button class="btn btn-primary" (click)="loadAnalytics()">Apply</button>
  </div>
</div>
    <app-summary-cards [summary]="summary"></app-summary-cards>
    <app-line-chart [data]="signupData"></app-line-chart>
    <app-pie-chart [data]="deviceData" title="Device Usage"></app-pie-chart>
  `
})
export class DashboardComponent implements OnInit {
  summary: { users: number; active: number } | null = null;
  signupData: number[] = [];
  deviceData: any = {};

  startDate: string = '';
endDate: string = '';

  constructor(
    private analyticsService: AnalyticsService,
    public featureService: FeatureToggleService
  ) {}


  ngOnInit() {
    this.analyticsService.getSummary().subscribe((data: SummaryApiResponse) => {
      this.summary = {
        users: data.totalUsers,
        active: data.activeUsers
      };
    });
    this.analyticsService.getSignups().subscribe(data => this.signupData = data);
    this.analyticsService.getDevices().subscribe(data => this.deviceData = data);
  }

  loadAnalytics() {
  this.analyticsService.getSummaryDate(this.startDate, this.endDate).subscribe(data => {
    this.summary = { users: data.totalUsers, active: data.activeUsers };
  });
}
}
//    //<app-summary-cards [summary]="{users: 10, active: 5}"></app-summary-cards>