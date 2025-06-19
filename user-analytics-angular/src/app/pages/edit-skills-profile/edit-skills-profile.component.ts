import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileEditService } from '../../profile-edit.service';

@Component({
  selector: 'app-edit-skills-profile',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './edit-skills-profile.component.html',
  styleUrl: './edit-skills-profile.component.css'
})
export class EditSkillsProfileComponent {
  programmingLanguages = '';
  webFrameworks = '';
  devOps = '';
  sql = '';
  vcs = '';
  tools = '';

  constructor(private profileEditService: ProfileEditService) {}

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
}