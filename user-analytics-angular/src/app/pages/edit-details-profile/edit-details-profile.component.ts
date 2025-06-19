import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../profile-edit.service';

@Component({
  selector: 'app-edit-details-profile',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './edit-details-profile.component.html',
  styleUrl: './edit-details-profile.component.css'
})
export class EditDetailsProfileComponent {
  notification = '';
  message = '';
  staff = '';
  bio = '';

  constructor(private profileEditService: ProfileEditService) {}

  updateDetails() {
    const payload = {
      notification: this.notification,
      message: this.message,
      staff: this.staff,
      bio: this.bio
    };
    this.profileEditService.updateDetails(payload).subscribe({
      next: (res: any) => {
        // handle success, e.g. show a message
        console.log('Details updated', res);
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Update failed', err);
      }
    });
  }
}