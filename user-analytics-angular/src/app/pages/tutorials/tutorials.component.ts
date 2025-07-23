import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SafePipe } from '../../shared/safe.pipe';

@Component({
  selector: 'app-tutorials',
  standalone: true,
  imports: [CommonModule, SafePipe],
  templateUrl: './tutorials.component.html',
  styleUrl: './tutorials.component.css'
})
export class TutorialsComponent {
    videos = [
    {
      title: 'Getting Started with the Dashboard',
      description: 'Overview of main features and navigation.',
      url: 'https://www.youtube.com/watch?v=RRubcjpTkks'
    },
    {
      title: 'How to Manage Your Profile',
      description: 'Step-by-step profile editing.',
      url: 'https://www.youtube.com/watch?v=RRubcjpTkks'
    }
  ];

}
