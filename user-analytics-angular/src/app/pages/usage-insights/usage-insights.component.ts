import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgChartsModule } from 'ng2-charts';
import { UsageService } from '../../services/usage.service';

@Component({
  selector: 'app-usage-insights',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  templateUrl: './usage-insights.component.html',
  styleUrls: ['./usage-insights.component.css']
})
export class UsageInsightsComponent implements OnInit {
  labels: string[] = [];
  usageData: any;

  constructor(private usageService: UsageService) { }

  ngOnInit(): void {
    this.usageService.getFeatureUsage().subscribe(data => {
      console.log(data);
      this.labels = data.features;
      this.usageData = {
        labels: data.features,
        datasets: [{ data: data.usageCounts, label: 'Usage Count' }]
      };
    });
  }

}

