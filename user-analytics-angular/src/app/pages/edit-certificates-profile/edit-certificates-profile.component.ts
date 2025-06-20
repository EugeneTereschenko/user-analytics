import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../profile-edit.service';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
  selector: 'app-edit-certificates-profile',
  standalone: true,
  imports: [RouterModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule],
  templateUrl: './edit-certificates-profile.component.html',
  styleUrl: './edit-certificates-profile.component.css'
})
export class EditCertificatesProfileComponent {

  certificateName: string = '';
  dateFrom: string = '';
  dateTo: string = '';

  constructor(private profileEditService: ProfileEditService) {}

  // In your component
  updateCertificateDates() {
    const payload = {
      certificateName: this.certificateName,
      dateFrom: this.dateFrom,
      dateTo: this.dateTo
    };
    this.profileEditService.updateCertificateDates(payload).subscribe({
      next: (res: any) => {
        // handle success
        console.log('Certificate dates updated', res);
      },
      error: (err: any) => {
        // handle error
        console.error('Update failed', err);
      }
    });
  }

  refresh() {
    // Implement refresh logic if needed
  }
}