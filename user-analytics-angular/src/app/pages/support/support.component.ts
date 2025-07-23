import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupportService } from '../../services/support-service.service'; // Adjust the path as necessary

@Component({
  selector: 'app-support',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './support.component.html',
  styleUrl: './support.component.css'
})
export class SupportComponent {
  subject = '';
  message = '';
  successMessage = '';

  constructor(private supportService: SupportService) {}


submitFeedback() {
  this.supportService.submitFeedback({ subject: this.subject, message: this.message })
    .subscribe(() => {
      this.successMessage = 'Thank you for your feedback!';
      this.subject = '';
      this.message = '';
    });
}


}
