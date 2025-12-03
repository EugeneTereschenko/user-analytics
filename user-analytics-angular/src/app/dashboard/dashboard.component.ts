import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SummaryCardsComponent } from '../summary-cards/summary-cards.component';
import { LineChartComponent } from '../line-chart/line-chart.component';
import { PieChartComponent } from '../pie-chart/pie-chart.component';           // <-- adjust path as needed
import { AnalyticsService } from '../services/analytics.service';
import { NgChartsModule } from 'ng2-charts';
import { FeatureToggleService } from '../services/feature-toggle.service';
import { FormsModule } from '@angular/forms';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { OnboardingService } from '../services/onboarding.service';

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
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  summary: { users: number; active: number } | null = null;
  signupData: number[] = [];
  deviceData: any = {};

  startDate: string = new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString().slice(0, 10);
  endDate: string = new Date().toISOString().slice(0, 10);

  constructor(
    private analyticsService: AnalyticsService,
    public featureService: FeatureToggleService,
    private translateService: TranslateService,
    private onboarding: OnboardingService
  ) {}

  ngOnInit() {
    this.loadSummary();
    this.loadSignups();
    this.loadDevices();
    // Use the current language set in TranslateService
    const currentLang = this.translateService.currentLang || this.translateService.getDefaultLang();
    this.translateService.use(currentLang);
  }

  start() {
    this.onboarding.startTour();
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