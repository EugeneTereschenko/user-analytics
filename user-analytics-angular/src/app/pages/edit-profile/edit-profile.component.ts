import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../profile-edit.service';

import { OnInit } from '@angular/core';

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


  ngAfterViewInit() {
    this.refresh();
  }

  ngOnInit() {
    this.readpayload();
  }

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
  
  readpayload() {
    this.profileEditService.getProfile().subscribe({
      next: (res: any) => {
        // handle success, e.g. populate the form with existing data
        this.firstName = res.firstName || '';
        this.lastName = res.lastName || '';
        this.email = res.email || '';
        this.linkedin = res.linkedin || '';
        this.skype = res.skype || '';
        this.github = res.github || '';
        this.address = res.address || '';
        this.shippingAddress = res.shippingAddress || '';
        this.phone = res.phone || '';
        this.refresh();
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Failed to load profile', err);
      }
    });
  }

  refresh() {
    const profileInformation = document.getElementById('profile-information');
    if (profileInformation) {
      profileInformation.innerHTML = ''; // Clear existing content
      const row = this.addEditProfileRow();
      profileInformation.appendChild(row);
    }
  }


  addEditProfileRow(): HTMLElement {
    const row = document.createElement('div');
    row.className = 'row';

    const col1 = this.addEditProfileColumn('First Name', this.firstName);
    const col2 = this.addEditProfileColumn('Last Name', this.lastName);
    const col3 = this.addEditProfileColumn('Email', this.email);
    const col4 = this.addEditProfileColumn('Phone', this.phone);
    const col5 = this.addEditProfileColumn('LinkedIn', this.linkedin);
    const col6 = this.addEditProfileColumn('Skype', this.skype);
    const col7 = this.addEditProfileColumn('GitHub', this.github);
    const col8 = this.addEditProfileColumn('Address', this.address);
    const col9 = this.addEditProfileColumn('Shipping Address', this.shippingAddress);
    const col10 = this.addEditProfileColumn('Phone', this.phone);



    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);
    row.appendChild(col4);
    row.appendChild(col5);
    row.appendChild(col6);
    row.appendChild(col7);
    row.appendChild(col8);
    row.appendChild(col9);
    row.appendChild(col10);

    return row;
  }


  addEditProfileColumn(title: string, text: string): HTMLElement {
  const col = document.createElement('div');
  col.className = 'col-6 mb-3 large-title';

  const h6 = document.createElement('h6');
  h6.className = 'large-title';
  h6.style.fontSize = '1.0rem';
  h6.textContent = title;

  const p = document.createElement('p');
  p.className = 'text-muted';
  p.textContent = text;

  col.appendChild(h6);
  col.appendChild(p);

  return col;
  }
}
