// summary-cards.component.ts
import { Component, Input } from '@angular/core';
import { NgChartsModule } from 'ng2-charts';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-summary-cards',
    standalone: true,
    imports: [CommonModule, NgChartsModule],
  template: `
    <div class="card" *ngIf="summary">
      <p><strong>Total Users:</strong> {{ summary.users }}</p>
      <p><strong>Active Users:</strong> {{ summary.active }}</p>
    </div>
  `
})
export class SummaryCardsComponent {
  @Input() summary: { users: number; active: number } | null = null;

  ngOnChanges() {
    console.log('Summary input changed:', this.summary);
  }
}
