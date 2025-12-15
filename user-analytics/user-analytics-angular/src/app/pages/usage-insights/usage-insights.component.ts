import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgChartsModule, BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartType } from 'chart.js';
import { Subscription } from 'rxjs';
import { UsageService, UsageStatistics, FeatureUsageDTO, UsageTrend, FeatureDetail } from '../../services/usage.service';

@Component({
  selector: 'app-usage-insights',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  templateUrl: './usage-insights.component.html',
  styleUrls: ['./usage-insights.component.css']
})
export class UsageInsightsComponent implements OnInit, OnDestroy {
  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;

  // Data
  statistics: UsageStatistics | null = null;
  featureDetails: FeatureDetail[] = [];
  topFeatures: FeatureDetail[] = [];
  
  // Chart data
  barChartData: ChartConfiguration['data'] = {
    labels: [],
    datasets: []
  };
  
  lineChartData: ChartConfiguration['data'] = {
    labels: [],
    datasets: []
  };
  
  pieChartData: ChartConfiguration['data'] = {
    labels: [],
    datasets: []
  };

  // Chart options
  barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        position: 'top'
      },
      tooltip: {
        callbacks: {
          label: (context) => {
            return `${context.dataset.label}: ${context.parsed.y} users`;
          }
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          precision: 0
        }
      }
    }
  };

  lineChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        position: 'top'
      }
    },
    scales: {
      y: {
        beginAtZero: true
      }
    }
  };

  pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        position: 'right'
      }
    }
  };

  // Chart types
  barChartType: ChartType = 'bar';
  lineChartType: ChartType = 'line';
  pieChartType: ChartType = 'pie';

  // UI State
  selectedPeriod: 'daily' | 'weekly' | 'monthly' | 'yearly' = 'weekly';
  selectedChart: 'bar' | 'line' | 'pie' = 'bar';
  isLoading = false;
  errorMessage = '';

  // Expose Math to template
  Math = Math;

  private subscriptions = new Subscription();

  constructor(private usageService: UsageService) {}

  ngOnInit(): void {
    this.loadAllData();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  loadAllData(): void {
    this.isLoading = true;
    this.errorMessage = '';

    // Load statistics
    this.subscriptions.add(
      this.usageService.getStatistics().subscribe({
        next: (stats) => {
          this.statistics = stats;
        },
        error: (error) => {
          console.error('Error loading statistics:', error);
          this.errorMessage = 'Failed to load statistics';
        }
      })
    );

    // Load feature usage
    this.loadFeatureUsage(this.selectedPeriod);

    // Load feature details
    this.subscriptions.add(
      this.usageService.getFeatureDetails().subscribe({
        next: (details) => {
          this.featureDetails = details;
        },
        error: (error) => {
          console.error('Error loading feature details:', error);
        }
      })
    );

    // Load top features
    this.subscriptions.add(
      this.usageService.getTopFeatures(5).subscribe({
        next: (top) => {
          this.topFeatures = top;
        },
        error: (error) => {
          console.error('Error loading top features:', error);
        }
      })
    );

    // Load usage trends
    this.loadUsageTrends(this.selectedPeriod);

    // Load category breakdown
    this.loadCategoryBreakdown();

    this.isLoading = false;
  }

  loadFeatureUsage(period: string): void {
    this.subscriptions.add(
      this.usageService.getFeatureUsage(period).subscribe({
        next: (data) => {
          this.updateBarChart(data);
        },
        error: (error) => {
          console.error('Error loading feature usage:', error);
        }
      })
    );
  }

  loadUsageTrends(period: string): void {
    this.subscriptions.add(
      this.usageService.getUsageTrends(period).subscribe({
        next: (trend) => {
          this.updateLineChart(trend);
        },
        error: (error) => {
          console.error('Error loading usage trends:', error);
        }
      })
    );
  }

  loadCategoryBreakdown(): void {
    this.subscriptions.add(
      this.usageService.getUsageByCategory().subscribe({
        next: (data) => {
          this.updatePieChart(data);
        },
        error: (error) => {
          console.error('Error loading category breakdown:', error);
        }
      })
    );
  }

  updateBarChart(data: FeatureUsageDTO): void {
    this.barChartData = {
      labels: data.features,
      datasets: [
        {
          label: 'Usage Count',
          data: data.usageCounts,
          backgroundColor: 'rgba(102, 126, 234, 0.7)',
          borderColor: 'rgba(102, 126, 234, 1)',
          borderWidth: 2
        }
      ]
    };
    this.chart?.update();
  }

  updateLineChart(trend: UsageTrend): void {
    const labels = trend.data.map(d => d.date);
    const values = trend.data.map(d => d.value);

    this.lineChartData = {
      labels: labels,
      datasets: [
        {
          label: 'Usage Trend',
          data: values,
          borderColor: 'rgba(102, 126, 234, 1)',
          backgroundColor: 'rgba(102, 126, 234, 0.1)',
          fill: true,
          tension: 0.4
        }
      ]
    };
  }

  updatePieChart(data: any): void {
    this.pieChartData = {
      labels: data.categories,
      datasets: [
        {
          data: data.values,
          backgroundColor: [
            'rgba(102, 126, 234, 0.8)',
            'rgba(118, 75, 162, 0.8)',
            'rgba(255, 193, 7, 0.8)',
            'rgba(13, 202, 240, 0.8)',
            'rgba(25, 135, 84, 0.8)'
          ]
        }
      ]
    };
  }

  changePeriod(period: 'daily' | 'weekly' | 'monthly' | 'yearly'): void {
    this.selectedPeriod = period;
    this.loadFeatureUsage(period);
    this.loadUsageTrends(period);
  }

  changeChart(type: 'bar' | 'line' | 'pie'): void {
    this.selectedChart = type;
  }

  refreshData(): void {
    this.loadAllData();
  }

  exportData(format: 'json' | 'csv'): void {
    this.usageService.exportUsageData(format).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `usage-insights-${new Date().toISOString()}.${format}`;
        link.click();
        URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Error exporting data:', error);
        alert('Failed to export data');
      }
    });
  }

  getStars(rating: number): string[] {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

    const stars: string[] = [];
    for (let i = 0; i < fullStars; i++) stars.push('bi-star-fill');
    if (hasHalfStar) stars.push('bi-star-half');
    for (let i = 0; i < emptyStars; i++) stars.push('bi-star');

    return stars;
  }

  getGrowthClass(growth: number): string {
    if (growth > 5) return 'badge bg-success';
    if (growth < -5) return 'badge bg-danger';
    return 'badge bg-warning';
  }

  getGrowthIcon(growth: number): string {
    if (growth > 0) return 'bi-arrow-up';
    if (growth < 0) return 'bi-arrow-down';
    return 'bi-dash';
  }

  formatPercentage(value: number): string {
    return `${value.toFixed(1)}%`;
  }

  formatNumber(value: number): string {
    return value.toLocaleString();
  }
}

