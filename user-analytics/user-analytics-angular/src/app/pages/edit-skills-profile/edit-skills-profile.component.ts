import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../services/profile-edit.service';


import { OnInit } from '@angular/core';

@Component({
  selector: 'app-edit-skills-profile',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './edit-skills-profile.component.html',
  styleUrl: './edit-skills-profile.component.css'
})
export class EditSkillsProfileComponent {
    @ViewChild('profileSkills', { static: false }) profileSkillsRef!: ElementRef;
  programmingLanguages = '';
  webFrameworks = '';
  devOps = '';
  sql = '';
  vcs = '';
  tools = '';

  constructor(private profileEditService: ProfileEditService) {}

  ngAfterViewInit() {
    this.refresh();
  }

  ngOnInit() {
    this.readpayload();
  }

  saveSkills() {
    const payload = {
      programmingLanguages: this.programmingLanguages,
      webFrameworks: this.webFrameworks,
      devOps: this.devOps,
      sql: this.sql,
      vcs: this.vcs,
      tools: this.tools
    };
    this.profileEditService.saveSkills(payload).subscribe({
      next: (res: any) => {
        // handle success, e.g. show a message
        console.log('Skills updated', res);
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Update failed', err);
      }
    });
  }

  readpayload(){
    this.profileEditService.getSkills().subscribe({
      next: (res: any) => {
        // handle success, e.g. populate the form with existing data
        this.programmingLanguages = res.programmingLanguages || '';
        this.webFrameworks = res.webFrameworks || '';
        this.devOps = res.devOps || '';
        this.sql = res.sql || '';
        this.vcs = res.vcs || '';
        this.tools = res.tools || '';
        this.refresh();
      },
      error: (err: any) => {
        // handle error, e.g. show an error message
        console.error('Fetch failed', err);
      }
    });
  }

  refresh() {
    const profileSkillsElem = document.getElementById('profile-skills');
    if (profileSkillsElem) {
      profileSkillsElem.innerHTML = '';
      const row = this.addSkillRow();
      profileSkillsElem.appendChild(row);
    }
  }


  addSkillRow(): HTMLElement {
    const row = document.createElement('div');
    row.className = 'row';

    const col1 = this.addSkillColumn('Programming Languages', this.programmingLanguages);
    const col2 = this.addSkillColumn('WebFrameworks', this.webFrameworks);
    const col3 = this.addSkillColumn('DevOps', this.devOps);
    const col4 = this.addSkillColumn('SQL', this.sql);
    const col5 = this.addSkillColumn('VCS', this.vcs);
    const col6 = this.addSkillColumn('TOOLS', this.tools);

    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);
    row.appendChild(col4);
    row.appendChild(col5);
    row.appendChild(col6);

    return row;
  }

  addSkillColumn(title: string, text: string): HTMLElement {
  const col = document.createElement('div');
  col.className = 'col-6 mb-3';

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