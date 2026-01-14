import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { BillingService } from '../../../services/billing.service';
import { Invoice, InvoiceStatus } from '../../../models/billing.model';
import { PaymentFormComponent } from '../payment-form/payment-form.component';

@Component({
  selector: 'app-invoice-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, PaymentFormComponent],
  templateUrl: './invoice-list.component.html',
  styleUrls: ['./invoice-list.component.css']
})
export class InvoiceListComponent implements OnInit {
  invoices: Invoice[] = [];
  filteredInvoices: Invoice[] = [];
  selectedInvoice: Invoice | null = null;
  
  // Pagination
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  
  // Filters
  searchQuery = '';
  statusFilter: InvoiceStatus | 'ALL' = 'ALL';
  patientIdFilter = '';
  
  // Stats
  totalOutstanding = 0;
  overdueCount = 0;
  
  // Loading state
  isLoading = false;
  error: string | null = null;
  
  // Enums for template
  invoiceStatuses = Object.values(InvoiceStatus);
  Math = Math;
  
  constructor(private billingService: BillingService) { }

  ngOnInit(): void {
    this.loadInvoices();
    this.loadStats();
  }

  loadInvoices(): void {
    this.isLoading = true;
    this.error = null;
    
    this.billingService.getAllInvoices(this.currentPage, this.pageSize)
      .subscribe({
        next: (data) => {
          this.invoices = data.content;
          this.filteredInvoices = data.content;
          this.totalElements = data.totalElements;
          this.totalPages = data.totalPages;
          this.isLoading = false;
        },
        error: (err) => {
          this.error = 'Failed to load invoices';
          console.error(err);
          this.isLoading = false;
        }
      });
  }

  loadStats(): void {
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
  }

  searchInvoices(): void {
    if (this.searchQuery.trim()) {
      this.isLoading = true;
      this.billingService.searchInvoices(this.searchQuery, this.currentPage, this.pageSize)
        .subscribe({
          next: (data) => {
            this.filteredInvoices = data.content;
            this.totalElements = data.totalElements;
            this.totalPages = data.totalPages;
            this.isLoading = false;
          },
          error: (err) => {
            this.error = 'Search failed';
            console.error(err);
            this.isLoading = false;
          }
        });
    } else {
      this.loadInvoices();
    }
  }

  filterByStatus(): void {
    if (this.statusFilter !== 'ALL') {
      this.isLoading = true;
      this.billingService.getInvoicesByStatus(this.statusFilter as InvoiceStatus, this.currentPage, this.pageSize)
        .subscribe({
          next: (data) => {
            this.filteredInvoices = data.content;
            this.totalElements = data.totalElements;
            this.totalPages = data.totalPages;
            this.isLoading = false;
          },
          error: (err) => {
            this.error = 'Filter failed';
            console.error(err);
            this.isLoading = false;
          }
        });
    } else {
      this.loadInvoices();
    }
  }

  filterByPatient(): void {
    if (this.patientIdFilter) {
      this.isLoading = true;
      this.billingService.getInvoicesByPatient(+this.patientIdFilter, this.currentPage, this.pageSize)
        .subscribe({
          next: (data) => {
            this.filteredInvoices = data.content;
            this.totalElements = data.totalElements;
            this.totalPages = data.totalPages;
            this.isLoading = false;
          },
          error: (err) => {
            this.error = 'Filter failed';
            console.error(err);
            this.isLoading = false;
          }
        });
    } else {
      this.loadInvoices();
    }
  }

  viewInvoice(invoice: Invoice): void {
    this.selectedInvoice = invoice;
  }

  sendInvoice(id: number): void {
    if (confirm('Are you sure you want to send this invoice to the patient?')) {
      this.billingService.sendInvoice(id)
        .subscribe({
          next: () => {
            this.loadInvoices();
            alert('Invoice sent successfully');
          },
          error: (err) => {
            alert('Failed to send invoice');
            console.error(err);
          }
        });
    }
  }

  showAddPaymentModal(invoice: Invoice): void {
    this.selectedInvoice = invoice;
  }

  cancelInvoice(id: number): void {
    if (confirm('Are you sure you want to cancel this invoice?')) {
      this.billingService.cancelInvoice(id)
        .subscribe({
          next: () => {
            this.loadInvoices();
            alert('Invoice cancelled successfully');
          },
          error: (err) => {
            alert('Failed to cancel invoice');
            console.error(err);
          }
        });
    }
  }

  deleteInvoice(id: number): void {
    if (confirm('Are you sure you want to delete this invoice? This action cannot be undone.')) {
      this.billingService.deleteInvoice(id)
        .subscribe({
          next: () => {
            this.loadInvoices();
            alert('Invoice deleted successfully');
          },
          error: (err) => {
            alert('Failed to delete invoice');
            console.error(err);
          }
        });
    }
  }

  getStatusClass(status: InvoiceStatus): string {
    const statusClasses: Record<InvoiceStatus, string> = {
      [InvoiceStatus.DRAFT]: 'badge bg-secondary',
      [InvoiceStatus.PENDING]: 'badge bg-warning text-dark',
      [InvoiceStatus.SENT]: 'badge bg-info',
      [InvoiceStatus.PARTIALLY_PAID]: 'badge bg-primary',
      [InvoiceStatus.PAID]: 'badge bg-success',
      [InvoiceStatus.OVERDUE]: 'badge bg-danger',
      [InvoiceStatus.CANCELLED]: 'badge bg-dark',
      [InvoiceStatus.REFUNDED]: 'badge bg-secondary'
    };
    return statusClasses[status];
  }

  isOverdue(invoice: Invoice): boolean {
    if (!invoice.dueDate) return false;
    const today = new Date();
    const dueDate = new Date(invoice.dueDate);
    return dueDate < today && invoice.status !== InvoiceStatus.PAID && invoice.status !== InvoiceStatus.CANCELLED;
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadInvoices();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadInvoices();
    }
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadInvoices();
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  onPaymentAdded(): void {
    this.loadInvoices();
    this.loadStats();
  }
}
