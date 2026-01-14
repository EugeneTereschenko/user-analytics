import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BillingService } from '../../../services/billing.service';
import { Invoice, Payment } from '../../../models/billing.model';

@Component({
  selector: 'app-billing-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './billing-dashboard.component.html',
  styleUrls: ['./billing-dashboard.component.css']
})
export class BillingDashboardComponent implements OnInit {
  totalRevenue = 0;
  totalOutstanding = 0;
  overdueCount = 0;
  pendingCount = 0;
  recentInvoices: Invoice[] = [];
  recentPayments: Payment[] = [];

  constructor(private billingService: BillingService) { }

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.billingService.getTotalOutstanding()
      .subscribe({
        next: (total) => this.totalOutstanding = total,
        error: (err) => console.error('Failed to load outstanding total', err)
      });

    this.billingService.getOverdueInvoices()
      .subscribe({
        next: (invoices) => this.overdueCount = invoices.length,
        error: (err) => console.error('Failed to load overdue invoices', err)
      });

    this.billingService.getAllInvoices(0, 5)
      .subscribe({
        next: (data) => {
          this.recentInvoices = data.content;
          // Calculate total revenue from paid invoices
          this.billingService.getAllInvoices(0, 100)
            .subscribe({
              next: (allData) => {
                this.totalRevenue = allData.content
                  .filter(inv => inv.status === 'PAID')
                  .reduce((sum, inv) => sum + inv.paidAmount, 0);
                
                this.pendingCount = allData.content
                  .filter(inv => inv.status === 'PENDING' || inv.status === 'SENT')
                  .length;
              },
              error: (err) => console.error('Failed to calculate revenue', err)
            });
        },
        error: (err) => console.error('Failed to load recent invoices', err)
      });
  }
}
