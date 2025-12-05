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

  ngAfterViewInit() {
    this.refresh();
  }

  ngOnInit() {
    this.readpayload();
  }

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

  readpayload() {
    this.profileEditService.getProjects().subscribe({
      next: (res: any) => {
        // handle success, e.g. populate the form with existing data
        this.projectName = res.projectName || '';
        this.dateFrom = res.dateFrom || '';
        this.dateTo = res.dateTo || '';
        this.structure = res.structure || '';
        this.refresh();
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Failed to load projects', err);
      }
    });
  }

  refresh() {
    const profileProjects = document.getElementById('profile-projects');
    if (profileProjects) {
      profileProjects.innerHTML = ''; // Clear existing content
      const row = this.addProcejtsRow();
      profileProjects.appendChild(row);
    }
  }

  addProcejtsRow(): HTMLElement {
    const row = document.createElement('div');
    row.className = 'row';

    const col1 = this.addProjectsColumn('Project Name', this.projectName);
    const col2 = this.addProjectsColumn('Date From', this.dateFrom);
    const col3 = this.addProjectsColumn('Date To', this.dateTo);
    const col4 = this.addProjectsColumn('Structure', this.structure);


    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);
    row.appendChild(col4);

    return row;
  }

  addProjectsColumn(title: string, text: string): HTMLElement {
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