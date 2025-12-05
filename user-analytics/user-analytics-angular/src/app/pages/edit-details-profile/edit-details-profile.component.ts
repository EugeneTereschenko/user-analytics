import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../services/profile-edit.service';

import { OnInit } from '@angular/core';

@Component({
  selector: 'app-edit-details-profile',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './edit-details-profile.component.html',
  styleUrl: './edit-details-profile.component.css'
})
export class EditDetailsProfileComponent implements AfterViewInit, OnInit {
  @ViewChild('profileInformation', { static: false }) profileInformationRef!: ElementRef;

  notification = '';
  message = '';
  staff = '';
  bio = '';

  constructor(private profileEditService: ProfileEditService) {}

  ngAfterViewInit() {
    this.refresh();
  }

  ngOnInit() {
    this.readpayload();
  }

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

  readpayload() {
    this.profileEditService.getDetails().subscribe({
      next: (res: any) => {
        // handle success, e.g. populate the form with existing data
        this.notification = res.notification || '';
        this.message = res.message || '';
        this.staff = res.staff || '';
        this.bio = res.bio || '';
        this.refresh();
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Fetch failed', err);
      }
    });
  }

  refresh() {
    const parentRow = document.getElementById('profile-information');
    const newRow = this.addInformationRow();
    parentRow?.replaceChildren(newRow);
  }

  addInformationRow(): HTMLElement {
    const row = document.createElement('div');
    row.className = 'row';

    const col1 = this.addInformationColumn('Notification', this.notification);
    const col2 = this.addInformationColumn('Message', this.message);
    const col3 = this.addInformationColumn('Staff', this.staff);
    const col4 = this.addInformationColumn('Bio', this.bio);

    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);
    row.appendChild(col4);

    return row;
  }

  addInformationColumn(title: string, text: string): HTMLElement {
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