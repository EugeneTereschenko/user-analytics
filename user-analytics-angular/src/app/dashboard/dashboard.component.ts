import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SummaryCardsComponent } from '../summary-cards/summary-cards.component';
import { LineChartComponent } from '../line-chart/line-chart.component';
import { PieChartComponent } from '../pie-chart/pie-chart.component';           // <-- adjust path as needed
import { AnalyticsService } from '../analytics.service';
import { NgChartsModule } from 'ng2-charts';
import { FeatureToggleService } from '../feature-toggle.service';
import { FormsModule } from '@angular/forms';
import { TranslateService, TranslateModule } from '@ngx-translate/core';

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
    CommonModule,
    TranslateModule
  ],
  template: `

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <h2>{{ 'WELCOME' | translate }}</h2>
    <h2>Analytics Dashboard</h2>

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

    <ng-container *ngIf="featureService.current['featureNewChart']">
      <app-line-chart [data]="signupData"></app-line-chart>
    </ng-container>

    <app-pie-chart [data]="deviceData" title="Device Usage"></app-pie-chart>
  `
})
export class DashboardComponent implements OnInit {
  summary: { users: number; active: number } | null = null;
  signupData: number[] = [];
  deviceData: any = {};

  startDate: string = '2025-07-15';
  endDate: string = '2025-07-16';

  constructor(
    private analyticsService: AnalyticsService,
    public featureService: FeatureToggleService,
    private translateService: TranslateService
  ) {}

  ngOnInit() {
    this.loadSummary();
    this.loadSignups();
    this.loadDevices();
    // Use the current language set in TranslateService
    const currentLang = this.translateService.currentLang || this.translateService.getDefaultLang();
    this.translateService.use(currentLang);
  }

  loadSummary() {
    this.analyticsService.getSummaryDate(this.startDate, this.endDate).subscribe((data: SummaryApiResponse) => {
      this.summary = {
        users: data.totalUsers,
        active: data.activeUsers
      };
    });
  }

  loadSignups() {
    this.analyticsService.getSignups(this.startDate, this.endDate).subscribe(data => this.signupData = data);
  }

  loadDevices() {
    this.analyticsService.getDevices().subscribe(data => this.deviceData = data);
  }

  loadAnalytics() {
    this.analyticsService.getSummaryDate(this.startDate, this.endDate).subscribe(data => {
      this.summary = { users: data.totalUsers, active: data.activeUsers };
    });
  }
}
//    //<app-summary-cards [summary]="{users: 10, active: 5}"></app-summary-cards>