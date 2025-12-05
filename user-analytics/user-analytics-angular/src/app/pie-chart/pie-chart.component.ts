import { Component, Input } from '@angular/core';
import { NgChartsModule } from 'ng2-charts';

@Component({
  selector: 'app-pie-chart',
  standalone: true,
  imports: [NgChartsModule],
  template: `<canvas baseChart
              [data]="chartData"
              [labels]="labels"
              [type]="'pie'">
             </canvas>`
})
export class PieChartComponent {
  @Input() data: any = {};
  get labels() {
    return Object.keys(this.data);
  }
  get chartData() {
    return { datasets: [{ data: Object.values(this.data) }] };
  }
}