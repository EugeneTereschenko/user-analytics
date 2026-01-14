import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BillingService } from '../../../services/billing.service';
import { Invoice, InvoiceItem, InvoiceStatus, ItemType } from '../../../models/billing.model';

@Component({
  selector: 'app-invoice-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './invoice-form.component.html',
  styleUrls: ['./invoice-form.component.css']
})
export class InvoiceFormComponent implements OnInit {
  invoice: Invoice = {
    patientId: 0,
    invoiceDate: new Date().toISOString().split('T')[0],
    dueDate: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
    subtotal: 0,
    taxAmount: 0,
    discountAmount: 0,
    totalAmount: 0,
    paidAmount: 0,
    balanceDue: 0,
    status: InvoiceStatus.DRAFT,
    items: [],
    payments: [],
    insuranceClaimAmount: 0
  };

  isEditMode = false;
  isLoading = false;
  error: string | null = null;
  
  // Enums for template
  invoiceStatuses = Object.values(InvoiceStatus);
  itemTypes = Object.values(ItemType);

  constructor(
    private billingService: BillingService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loadInvoice(+id);
    }
  }

  loadInvoice(id: number): void {
    this.isLoading = true;
    this.billingService.getInvoiceById(id)
      .subscribe({
        next: (data) => {
          this.invoice = data;
          this.isLoading = false;
        },
        error: (err) => {
          this.error = 'Failed to load invoice';
          console.error(err);
          this.isLoading = false;
        }
      });
  }

  addItem(): void {
    const newItem: InvoiceItem = {
      itemType: ItemType.CONSULTATION,
      description: '',
      quantity: 1,
      unitPrice: 0,
      totalPrice: 0
    };
    this.invoice.items.push(newItem);
  }

  removeItem(index: number): void {
    this.invoice.items.splice(index, 1);
    this.calculateTotals();
  }

  calculateItemTotal(item: InvoiceItem): void {
    item.totalPrice = item.unitPrice * item.quantity;
    this.calculateTotals();
  }

  calculateTotals(): void {
    this.invoice.subtotal = this.invoice.items.reduce((sum, item) => sum + item.totalPrice, 0);
    this.invoice.totalAmount = this.invoice.subtotal + this.invoice.taxAmount - this.invoice.discountAmount;
    this.invoice.balanceDue = this.invoice.totalAmount - this.invoice.paidAmount;
  }

  onTaxChange(): void {
    this.calculateTotals();
  }

  onDiscountChange(): void {
    this.calculateTotals();
  }

  onSubmit(): void {
    this.isLoading = true;
    this.error = null;

    const operation = this.isEditMode
      ? this.billingService.updateInvoice(this.invoice.id!, this.invoice)
      : this.billingService.createInvoice(this.invoice);

    operation.subscribe({
      next: () => {
        this.router.navigate(['/billing/invoices']);
      },
      error: (err) => {
        this.error = this.isEditMode ? 'Failed to update invoice' : 'Failed to create invoice';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/billing/invoices']);
  }
}
