import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../profile-edit.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-certificates-profile',
  standalone: true,
  imports: [RouterModule, FormsModule],
  templateUrl: './edit-certificates-profile.component.html',
  styleUrl: './edit-certificates-profile.component.css'
})
export class EditCertificatesProfileComponent {

  certificateName: string = '';
  dateFrom: string = '';
  dateTo: string = '';

  constructor(private profileEditService: ProfileEditService) {}

  updateCertificateDates() {
    console.log('Updating certificate dates:', this.dateFrom, this.dateTo);
    const payload = {
      certificateName: this.certificateName,
      dateFrom: this.dateFrom,
      dateTo: this.dateTo
    };
    this.profileEditService.updateCertificateDates(payload).subscribe({
      next: (res: any) => {
        // handle success (e.g., show a message)
      },
      error: (err: any) => {
        // handle error (e.g., show an error message)
      }
    });
  }

  refresh() {
    // Implement refresh logic if needed
  }
}