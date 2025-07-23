// src/app/services/onboarding.service.ts
import { Injectable } from '@angular/core';
import Shepherd from 'shepherd.js';
import 'shepherd.js/dist/css/shepherd.css';

@Injectable({ providedIn: 'root' })
export class OnboardingService {
  private tour: Shepherd.Tour;

  constructor() {
    this.tour = new Shepherd.Tour({
      defaultStepOptions: {
        cancelIcon: { enabled: true },
        scrollTo: true,
        classes: 'shadow-md bg-purple-dark',
      }
    });

    this.tour.addStep({
      id: 'dashboard',
      text: 'Welcome to your dashboard!',
      attachTo: { element: '.dashboard-header', on: 'bottom' },
      buttons: [
        { text: 'Next', action: this.tour.next }
      ]
    });

    this.tour.addStep({
      id: 'profile',
      text: 'Edit your profile here.',
      attachTo: { element: '.profile-link', on: 'right' },
      buttons: [
        { text: 'Back', action: this.tour.back },
        { text: 'Done', action: this.tour.complete }
      ]
    });
  }

  startTour() {
    this.tour.start();
  }
}

