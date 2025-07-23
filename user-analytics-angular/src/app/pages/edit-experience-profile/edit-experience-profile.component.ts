import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../services/profile-edit.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

import { OnInit } from '@angular/core';

@Component({
  selector: 'app-edit-experience-profile',
  standalone: true,
  imports: [RouterModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule],
  templateUrl: './edit-experience-profile.component.html',
  styleUrl: './edit-experience-profile.component.css'
})
export class EditExperienceProfileComponent {
    @ViewChild('profileExperience', { static: false }) profileExperienceRef!: ElementRef;
  roleName = '';
  dateFrom = '';
  dateTo = '';
  companyName = '';
  countryCity = '';
  service = '';

  constructor(private profileEditService: ProfileEditService) {}

  ngAfterViewInit() {
    this.refresh();
  }

  ngOnInit() {
    this.readpayload();
  }
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

  refresh() {
    const profileExperience = document.getElementById('profile-experience-service');
    if (profileExperience) {
      profileExperience.innerHTML = ''; // Clear existing content
      const row = this.addExperienceRow();
      profileExperience.appendChild(row);
    }
  }
  readpayload() {
    this.profileEditService.getExperience().subscribe({
      next: (res: any) => {
        // handle success, e.g. populate the form with existing data
        this.roleName = res.roleName || '';
        this.dateFrom = res.dateFrom || '';
        this.dateTo = res.dateTo || '';
        this.companyName = res.companyName || '';
        this.countryCity = res.countryCity || '';
        this.service = res.service || '';
        this.refresh();
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Failed to load experience data', err);
      }
    });
  }

    addExperienceRow(): HTMLElement {
    const row = document.createElement('div');
    row.className = 'row';

    const col1 = this.addExperienceColumn('Role Name', this.roleName);
    const col2 = this.addExperienceColumn('Company Name', this.companyName);
    const col3 = this.addExperienceColumn('Date From', this.dateFrom);
    const col4 = this.addExperienceColumn('Date To', this.dateTo);
    const col5 = this.addExperienceColumn('Country/City', this.countryCity);
    const col6 = this.addExperienceColumn('Service', this.service);

    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);
    row.appendChild(col4);
    row.appendChild(col5);

    return row;
  }

  addExperienceColumn(title: string, text: string): HTMLElement {
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