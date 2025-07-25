import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileImageService } from '../../services/profile-image.service';
import { DomSanitizer } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

  profileImageUrl = 'https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava1-bg.webp';
  email = '';
  phone = '';
  recentProject = '';
  mostViewedProject = '';

  constructor(
    private profileImageService: ProfileImageService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit() {
    this.loadProfileImage();
    this.loadProfileInformation();
  }

  loadProfileImage() {
    this.profileImageService.loadProfileImage().subscribe({
      next: (blob: Blob) => {
        const objectURL = URL.createObjectURL(blob);
        this.profileImageUrl = this.sanitizer.bypassSecurityTrustUrl(objectURL) as string;
      },
      error: (err: any) => {
        console.error('Failed to load profile image', err);
      }
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    // Preview the image immediately
    const reader = new FileReader();
    reader.onload = () => {
      this.profileImageUrl = reader.result as string;
    };
    reader.readAsDataURL(file);

    // Upload the image
    this.profileImageService.uploadProfileImage(file).subscribe({
      next: (response: any) => {
        // Optionally update profileImageUrl with server response if needed
        // For now, we already previewed the image above
      },
      error: (err: any) => {
        // Handle upload error
        console.error('Image upload failed', err);
      }
    });
  }

  loadProfileInformation() {
  this.profileImageService.getProfileInformation().subscribe({
    next: (res: any) => {
      // Assign the received data to component properties
      this.email = res.email;
      this.phone = res.phone;
      this.recentProject = res.recentProject;
      this.mostViewedProject = res.mostViewedProject;
      // Add more assignments as needed
    },
    error: (err: any) => {
      console.error('Failed to load profile information', err);
    }
  });
}
}