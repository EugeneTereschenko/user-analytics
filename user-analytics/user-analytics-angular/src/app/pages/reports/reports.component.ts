import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgChartsModule } from 'ng2-charts';
import { Subscription } from 'rxjs';
import { ReportsService, ReportTemplate, ReportData, ReportFilter, ScheduledReport } from '../../services/reports.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, NgChartsModule],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.css'
})
export class ReportsComponent implements OnInit, OnDestroy {
  // Report templates
  reportTemplates: ReportTemplate[] = [];
  selectedTemplate: ReportTemplate | null = null;
  
  // Generated report
  currentReport: ReportData | null = null;
  savedReports: ReportData[] = [];
  scheduledReports: ScheduledReport[] = [];
  
  // Filters
  filters: ReportFilter = {
    startDate: this.getDefaultStartDate(),
    endDate: this.getDefaultEndDate()
  };
  
  // UI State
  isGenerating = false;
  showFilterModal = false;
  showScheduleModal = false;
  showSavedReports = false;
  selectedView: 'table' | 'chart' | 'summary' = 'summary';
  
  // Schedule form
  scheduleForm: ScheduledReport = {
    name: '',
    templateId: '',
    frequency: 'weekly',
    recipients: [],
    filters: {},
    enabled: true
  };
  
  recipientEmail = '';
  
  private subscriptions = new Subscription();

  constructor(private reportsService: ReportsService) {}

  ngOnInit(): void {
    this.loadReportTemplates();
    this.loadSavedReports();
    this.loadScheduledReports();
    
    // Subscribe to generation status
    this.subscriptions.add(
      this.reportsService.isGenerating().subscribe(status => {
        this.isGenerating = status;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  loadReportTemplates(): void {
    this.reportsService.getReportTemplates().subscribe({
      next: (templates) => {
        this.reportTemplates = templates;
      },
      error: (error) => {
        console.error('Error loading templates:', error);
      }
    });
  }

  loadSavedReports(): void {
    this.reportsService.getSavedReports().subscribe({
      next: (reports) => {
        this.savedReports = reports;
      },
      error: (error) => {
        console.error('Error loading saved reports:', error);
      }
    });
  }

  loadScheduledReports(): void {
    this.reportsService.getScheduledReports().subscribe({
      next: (reports) => {
        this.scheduledReports = reports;
      },
      error: (error) => {
        console.error('Error loading scheduled reports:', error);
      }
    });
  }

  selectTemplate(template: ReportTemplate): void {
    this.selectedTemplate = template;
    this.showFilterModal = true;
  }

  generateReport(): void {
    if (!this.selectedTemplate) return;
    
    this.reportsService.generateReport(this.selectedTemplate.id, this.filters).subscribe({
      next: (report) => {
        this.currentReport = report;
        this.showFilterModal = false;
        this.selectedView = 'summary';
      },
      error: (error) => {
        console.error('Error generating report:', error);
        alert('Failed to generate report. Please try again.');
      }
    });
  }

  generateQuickReport(type: string): void {
    let reportObservable: Observable<ReportData>;
    
    switch (type) {
      case 'user-activity':
        reportObservable = this.reportsService.getUserActivityReport(this.filters);
        break;
      case 'system-usage':
        reportObservable = this.reportsService.getSystemUsageReport(this.filters);
        break;
      case 'audit-logs':
        reportObservable = this.reportsService.getAuditLogReport(this.filters);
        break;
      case 'performance':
        reportObservable = this.reportsService.getPerformanceReport('weekly');
        break;
      default:
        return;
    }
    
    reportObservable.subscribe({
      next: (report) => {
        this.currentReport = report;
        this.selectedView = 'summary';
      },
      error: (error) => {
        console.error('Error generating quick report:', error);
      }
    });
  }

  exportReport(format: 'pdf' | 'excel' | 'csv'): void {
    if (!this.currentReport) return;
    
    this.reportsService.exportReport(this.currentReport, format).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `${this.currentReport!.title}_${new Date().toISOString()}.${format}`;
        link.click();
        URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Error exporting report:', error);
        alert('Failed to export report');
      }
    });
  }

  saveCurrentReport(): void {
    if (!this.currentReport) return;
    
    this.reportsService.saveReport(this.currentReport).subscribe({
      next: () => {
        alert('Report saved successfully!');
        this.loadSavedReports();
      },
      error: (error) => {
        console.error('Error saving report:', error);
        alert('Failed to save report');
      }
    });
  }

  loadSavedReport(report: ReportData): void {
    this.currentReport = report;
    this.showSavedReports = false;
    this.selectedView = 'summary';
  }

  deleteSavedReport(reportId: string): void {
    if (confirm('Are you sure you want to delete this report?')) {
      this.reportsService.deleteReport(reportId).subscribe({
        next: () => {
          this.loadSavedReports();
        },
        error: (error) => {
          console.error('Error deleting report:', error);
        }
      });
    }
  }

  openScheduleModal(): void {
    if (!this.selectedTemplate) return;
    this.scheduleForm.templateId = this.selectedTemplate.id;
    this.scheduleForm.filters = { ...this.filters };
    this.showScheduleModal = true;
  }

  addRecipient(): void {
    if (this.recipientEmail && this.isValidEmail(this.recipientEmail)) {
      if (!this.scheduleForm.recipients.includes(this.recipientEmail)) {
        this.scheduleForm.recipients.push(this.recipientEmail);
        this.recipientEmail = '';
      }
    }
  }

  removeRecipient(email: string): void {
    const index = this.scheduleForm.recipients.indexOf(email);
    if (index > -1) {
      this.scheduleForm.recipients.splice(index, 1);
    }
  }

  scheduleReport(): void {
    if (!this.scheduleForm.name || this.scheduleForm.recipients.length === 0) {
      alert('Please fill in all required fields');
      return;
    }
    
    this.reportsService.scheduleReport(this.scheduleForm).subscribe({
      next: () => {
        alert('Report scheduled successfully!');
        this.closeScheduleModal();
        this.loadScheduledReports();
      },
      error: (error) => {
        console.error('Error scheduling report:', error);
        alert('Failed to schedule report');
      }
    });
  }

  toggleScheduledReport(schedule: ScheduledReport): void {
    schedule.enabled = !schedule.enabled;
    if (schedule.id) {
      this.reportsService.updateScheduledReport(schedule.id, schedule).subscribe({
        error: (error) => {
          console.error('Error updating schedule:', error);
          schedule.enabled = !schedule.enabled;
        }
      });
    }
  }

  deleteScheduledReport(id: string): void {
    if (confirm('Are you sure you want to delete this scheduled report?')) {
      this.reportsService.deleteScheduledReport(id).subscribe({
        next: () => {
          this.loadScheduledReports();
        },
        error: (error) => {
          console.error('Error deleting schedule:', error);
        }
      });
    }
  }

  closeFilterModal(): void {
    this.showFilterModal = false;
    this.selectedTemplate = null;
  }

  closeScheduleModal(): void {
    this.showScheduleModal = false;
    this.scheduleForm = {
      name: '',
      templateId: '',
      frequency: 'weekly',
      recipients: [],
      filters: {},
      enabled: true
    };
    this.recipientEmail = '';
  }

  getReportsByCategory(category: string): ReportTemplate[] {
    return this.reportTemplates.filter(t => t.category === category);
  }

  getCategoryIcon(category: string): string {
    const icons: { [key: string]: string } = {
      'user': 'bi-people',
      'activity': 'bi-activity',
      'usage': 'bi-bar-chart',
      'system': 'bi-gear',
      'financial': 'bi-currency-dollar'
    };
    return icons[category] || 'bi-file-text';
  }

  getTrendIcon(type: string): string {
    return type === 'increase' ? 'bi-arrow-up' : 
           type === 'decrease' ? 'bi-arrow-down' : 'bi-dash';
  }

  getTrendClass(type: string): string {
    return type === 'increase' ? 'text-success' : 
           type === 'decrease' ? 'text-danger' : 'text-warning';
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  private getDefaultStartDate(): string {
    const date = new Date();
    date.setDate(date.getDate() - 30);
    return date.toISOString().split('T')[0];
  }

  private getDefaultEndDate(): string {
    return new Date().toISOString().split('T')[0];
  }

  private isValidEmail(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }

  printReport(): void {
    window.print();
  }

  refreshReport(): void {
    if (this.selectedTemplate) {
      this.generateReport();
    }
  }
}
