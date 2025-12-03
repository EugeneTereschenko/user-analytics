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
  uploadProgress: number = 0;


  constructor(private http: HttpClient, private fileService: FileService) {
    this.fetchFiles();
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  uploadFile() {
    if (!this.selectedFile) return;
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

  getFileType(filename: string): string {
    const ext = filename.split('.').pop()?.toLowerCase();
    if (['jpg', 'jpeg', 'png', 'gif', 'svg'].includes(ext || '')) return 'image';
    if (['mp4', 'avi', 'mov', 'mkv'].includes(ext || '')) return 'video';
    if (['zip', 'rar', '7z', 'tar'].includes(ext || '')) return 'archive';
    if (['pdf', 'doc', 'docx', 'txt'].includes(ext || '')) return 'document';
    return 'other';
  }

  getCurrentDate(): Date {
    return new Date();
  }

  getRandomSize(): string {
    const sizes = ['1.2 MB', '3.5 MB', '512 KB', '8.1 MB', '2.3 MB'];
    return sizes[Math.floor(Math.random() * sizes.length)];
  }
}
