import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../services/profile-edit.service';
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
  @ViewChild('profileEducation', { static: false }) profileEducationRef!: ElementRef;
  universityName = '';
  dateFrom = '';
  dateTo = '';
  countryCity = '';
  degree = '';

  constructor(private profileEditService: ProfileEditService) {}

  ngAfterViewInit() {
    this.refresh();
  }
  ngOnInit() {
    this.readpayload();
  }

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

  refresh() {
    // Implement refresh logic if needed, e.g. re-fetch data or update the view
    const profileEducation = document.getElementById('profile-education');
    if (!profileEducation) return;
    profileEducation.innerHTML = ''; // Clear existing content
    const educationRow = this.addEducationRow();
    profileEducation.appendChild(educationRow);
  }

  readpayload() {
    this.profileEditService.getEducation().subscribe({
      next: (res: any) => {
        // handle success, e.g. populate the form with existing data
        this.universityName = res.universityName || '';
        this.dateFrom = res.dateFrom || '';
        this.dateTo = res.dateTo || '';
        this.countryCity = res.countryCity || '';
        this.degree = res.degree || '';
        this.refresh(); // Refresh the view with the new data
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Fetch failed', err);
      }
    });
  }


  addEducationRow(): HTMLElement {
    const row = document.createElement('div');
    row.className = 'row';

    const col1 = this.addEducationColumn('University Name', this.universityName);
    const col2 = this.addEducationColumn('Date From', this.dateFrom);
    const col3 = this.addEducationColumn('Date To', this.dateTo);
    const col4 = this.addEducationColumn('Country/City', this.countryCity);
    const col5 = this.addEducationColumn('Degree', this.degree);

    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);
    row.appendChild(col4);
    row.appendChild(col5);

    return row;
  }

  addEducationColumn(title: string, text: string): HTMLElement {
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