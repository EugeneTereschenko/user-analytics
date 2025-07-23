import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FeatureToggleService } from '../../services/feature-toggle.service';
import { AsyncPipe } from '@angular/common'

@Component({
  selector: 'app-feature-toggle',
  standalone: true,
  imports: [CommonModule, AsyncPipe],
  templateUrl: './feature-toggle.component.html',
  styleUrl: './feature-toggle.component.css'
})
export class FeatureToggleComponent {
  toggles: { [key: string]: boolean } = {};
  featureKeys: string[] = [];

  constructor(private featureService: FeatureToggleService) {
    this.featureService.toggles$.subscribe(data => {
      this.toggles = data;
      this.featureKeys = Object.keys(data);
    });
  }

  onToggle(key: string, event: Event) {
    const input = event.target as HTMLInputElement;
    const value = input.checked;
    if (value) {
      this.featureService.enable(key);
    } else {
      this.featureService.disable(key);
    }
  }


}
