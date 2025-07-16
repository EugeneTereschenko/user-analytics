import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FeatureToggleService {
  // All toggles stored in a BehaviorSubject
  private toggles = new BehaviorSubject<{ [key: string]: boolean }>({
    featureNewChart: true,
    featureBetaProfile: false,
    featureDocumentation: true
  });

  toggles$ = this.toggles.asObservable();

  // Get current snapshot
  get current() {
    return this.toggles.value;
  }

  // Enable a feature
  enable(featureName: string) {
    this.update(featureName, true);
  }

  // Disable a feature
  disable(featureName: string) {
    this.update(featureName, false);
  }

  // Toggle a feature
  toggle(featureName: string) {
    const current = this.current[featureName];
    this.update(featureName, !current);
  }

  private update(featureName: string, enabled: boolean) {
    this.toggles.next({
      ...this.current,
      [featureName]: enabled
    });
  }
}

