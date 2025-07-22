import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FileService } from '../../services/file.service'; // Ensure this service is created

@Component({
  selector: 'app-file-manager',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './file-manager.component.html',
  styleUrl: './file-manager.component.css'
})
export class FileManagerComponent {
  files: string[] = [];
  selectedFile: File | null = null;

  constructor(private http: HttpClient, private fileService: FileService) {
    this.fetchFiles();
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  uploadFile() {
    if (!this.selectedFile) return;
    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.fileService.uploadFile(this.selectedFile).subscribe({
      next: () => {
        this.fetchFiles();
        this.selectedFile = null; // Reset selected file after upload
      },
      error: (err) => {
        console.error('File upload failed', err);
      }
    });
  }

  fetchFiles() {
    this.fileService.fetchFiles().subscribe(files => this.files = files);
  }

  downloadFile(fileName: string) {
    this.fileService.downloadFile(fileName).subscribe(blob => {
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = fileName;
      link.click();
    });
  }

  deleteFile(fileName: string) {
    this.fileService.deleteFile(fileName).subscribe({
      next: () => {
        this.fetchFiles();
      },
      error: (err) => {
        console.error('File deletion failed', err);
      }
    });
  }
}
