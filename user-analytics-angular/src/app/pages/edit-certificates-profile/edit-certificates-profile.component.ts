import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../profile-edit.service';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

import { OnInit } from '@angular/core';

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

  ngAfterViewInit() {
    this.refresh();
  }

  ngOnInit() {
    this.readpayload();
  }
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

  readpayload() {
    this.profileEditService.getCertificateDates().subscribe({
      next: (res: any) => {
        // handle success, e.g. populate the form with existing data
        this.certificateName = res.certificateName || '';
        this.dateFrom = res.dateFrom || '';
        this.dateTo = res.dateTo || '';
        this.refresh();
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Failed to load certificate dates', err);
      }
    });
  }


  refresh() {
    const profileCertificate = document.getElementById('profile-ertificate');
    if (!profileCertificate) return;
    profileCertificate.innerHTML = ''; // Clear existing content
    const row = this.addCertificateRow();
    profileCertificate.appendChild(row);
  }

  addCertificateRow(): HTMLElement {
   const row = document.createElement('div');
    row.className = 'row';

    const col1 = this.addCertificatesColumn('Certificate Name', this.certificateName);
    const col2 = this.addCertificatesColumn('Date From', this.dateFrom);
    const col3 = this.addCertificatesColumn('Date To', this.dateTo);


    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);


    return row;
  }
  
  addCertificatesColumn(title: string, text: string): HTMLElement {
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