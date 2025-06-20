import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../profile-edit.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
  selector: 'app-edit-projects-profile',
  standalone: true,
  imports: [RouterModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule],
  templateUrl: './edit-projects-profile.component.html',
  styleUrl: './edit-projects-profile.component.css'
})
export class EditProjectsProfileComponent {
  projectName = '';
  dateFrom = '';
  dateTo = '';
  structure = '';

  constructor(private profileEditService: ProfileEditService) {}

  updateProjects() {
    const payload = {
      projectName: this.projectName,
      dateFrom: this.dateFrom,
      dateTo: this.dateTo,
      structure: this.structure
    };
    this.profileEditService.updateProjects(payload).subscribe({
      next: (res: any) => {
        // handle success, e.g. show a message
        console.log('Projects updated', res);
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Update failed', err);
      }
    });
  }
}