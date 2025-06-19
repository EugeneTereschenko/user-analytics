import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../profile-edit.service';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.css'
})
export class EditProfileComponent {
  firstName = '';
  lastName = '';
  email = '';
  linkedin = '';
  skype = '';
  github = '';
  address = '';
  shippingAddress = '';
  phone = '';

  constructor(private profileEditService: ProfileEditService) {}

  updateProfile() {
    const payload = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      linkedin: this.linkedin,
      skype: this.skype,
      github: this.github,
      address: this.address,
      shippingAddress: this.shippingAddress,
      phone: this.phone
    };
    console.log('Updating profile with payload:', payload);
    this.profileEditService.updateProfile(payload).subscribe({
      next: (res: any) => {
        // handle success, e.g. show a message or navigate
        console.log('Profile updated', res);
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Update failed', err);
      }
    });
  }
}
