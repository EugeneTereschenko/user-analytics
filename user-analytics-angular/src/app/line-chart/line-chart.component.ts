import { Component, Input } from '@angular/core';
import { NgChartsModule } from 'ng2-charts';
import { ChartData } from 'chart.js';

@Component({
  selector: 'app-line-chart',
  standalone: true,
  imports: [NgChartsModule],
  template: `
    <canvas baseChart
            [data]="lineChartData"
            [labels]="lineChartLabels"
            [type]="'line'">
    </canvas>
  `
})
export class LineChartComponent {
  @Input() data: number[] = [];

  get lineChartData(): ChartData<'line'> {
    return {
      labels: this.lineChartLabels,
      datasets: [
        {
          data: this.data,
          label: 'Signups',
          fill: false,
          borderColor: '#3e95cd'
        }
      ]
    };
  }

  get lineChartLabels(): string[] {
    return this.data.map((_, i) => `Day ${i + 1}`);
  }
}