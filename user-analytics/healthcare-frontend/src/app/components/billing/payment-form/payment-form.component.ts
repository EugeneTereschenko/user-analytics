import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BillingService } from '../../../services/billing.service';
import { Invoice, Payment, PaymentMethod, PaymentStatus } from '../../../models/billing.model';

declare var bootstrap: any;

@Component({
  selector: 'app-payment-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './payment-form.component.html',
  styleUrls: ['./payment-form.component.css']
})
export class PaymentFormComponent implements OnInit {
  @Input() invoice!: Invoice;
  @Output() paymentAdded = new EventEmitter<void>();

  payment: Payment = {
    paymentDate: new Date().toISOString().slice(0, 16),
    amount: 0,
    paymentMethod: PaymentMethod.CASH,
    status: PaymentStatus.COMPLETED
  };

  isSubmitting = false;
  error: string | null = null;

  paymentMethods = Object.values(PaymentMethod);
  paymentStatuses = Object.values(PaymentStatus);

  constructor(private billingService: BillingService) { }

  ngOnInit(): void {
    if (this.invoice) {
      this.payment.amount = this.invoice.balanceDue;
    }
  }

  onSubmit(): void {
    this.isSubmitting = true;
    this.error = null;

    this.billingService.addPayment(this.invoice.id!, this.payment)
      .subscribe({
        next: () => {
          this.paymentAdded.emit();
          this.isSubmitting = false;
          this.closeModal();
        },
        error: (err) => {
          this.error = 'Failed to add payment';
          console.error(err);
          this.isSubmitting = false;
        }
      });
  }

  closeModal(): void {
    const modal = document.getElementById('addPaymentModal');
    if (modal) {
      const bsModal = bootstrap.Modal.getInstance(modal);
      if (bsModal) {
        bsModal.hide();
      }
    }
  }
}
