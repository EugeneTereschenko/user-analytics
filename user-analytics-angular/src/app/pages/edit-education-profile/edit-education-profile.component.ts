import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../profile-edit.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
  selector: 'app-edit-education-profile',
  standalone: true,
  imports: [RouterModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule],
  templateUrl: './edit-education-profile.component.html',
  styleUrl: './edit-education-profile.component.css'
})
export class EditEducationProfileComponent {
  universityName = '';
  dateFrom = '';
  dateTo = '';
  countryCity = '';
  degree = '';

  constructor(private profileEditService: ProfileEditService) {}

  updateEducation() {
    const payload = {
      universityName: this.universityName,
      dateFrom: this.dateFrom,
      dateTo: this.dateTo,
      countryCity: this.countryCity,
      degree: this.degree
    };
    this.profileEditService.updateEducation(payload).subscribe({
      next: (res: any) => {
        // handle success, e.g. show a message
        console.log('Education updated', res);
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Update failed', err);
      }
    });
  }
}