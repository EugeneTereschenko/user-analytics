import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../profile-edit.service';

@Component({
  selector: 'app-edit-experience-profile',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './edit-experience-profile.component.html',
  styleUrl: './edit-experience-profile.component.css'
})
export class EditExperienceProfileComponent {
  roleName = '';
  dateFrom = '';
  dateTo = '';
  companyName = '';
  countryCity = '';
  service = '';

  constructor(private profileEditService: ProfileEditService) {}

  updateExperience() {
    const payload = {
      roleName: this.roleName,
      dateFrom: this.dateFrom,
      dateTo: this.dateTo,
      companyName: this.companyName,
      countryCity: this.countryCity,
      service: this.service
    };
    this.profileEditService.updateExperience(payload).subscribe({
      next: (res: any) => {
        // handle success, e.g. show a message
        console.log('Experience updated', res);
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Update failed', err);
      }
    });
  }
}