import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SummaryCardsComponent } from '../summary-cards/summary-cards.component';
import { LineChartComponent } from '../line-chart/line-chart.component';
import { PieChartComponent } from '../pie-chart/pie-chart.component';           // <-- adjust path as needed
import { AnalyticsService } from '../analytics.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    SummaryCardsComponent,
    LineChartComponent,
    PieChartComponent
  ],
  template: `
    <h2>Analytics Dashboard</h2>
    <app-line-chart [data]="signupData"></app-line-chart>
    <app-pie-chart [data]="deviceData" title="Device Usage"></app-pie-chart>
  `
})
export class DashboardComponent implements OnInit {
  summary: any;
  signupData: number[] = [];
  deviceData: any = {};

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit() {
    this.analyticsService.getSummary().subscribe(data => this.summary = data);
    this.analyticsService.getSignups().subscribe(data => this.signupData = data);
    this.analyticsService.getDevices().subscribe(data => this.deviceData = data);
  }
}
//    //<app-summary-cards [summary]="{users: 10, active: 5}"></app-summary-cards>