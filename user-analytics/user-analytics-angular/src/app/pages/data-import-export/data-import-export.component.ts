import { Component } from '@angular/core';
import { DataimportexportService } from '../../services/dataimportexport.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-data-import-export',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './data-import-export.component.html',
  styleUrl: './data-import-export.component.css',
  providers: [DataimportexportService]
})
export class DataImportExportComponent {
  importedData: any[] = [];
  previewHeaders: string[] = [];

  constructor(private dataService: DataimportexportService) {}

  async onFileSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    try {
      const result = await this.dataService.parseFile(file);
      this.importedData = result.data;
      this.previewHeaders = result.headers;
    } catch (err) {
      alert((err && typeof err === 'object' && 'message' in err) ? (err as any).message : 'Error parsing file.');
    }
  }

  exportCSV() {
    try {
      this.dataService.exportToCSV(this.importedData);
    } catch (err) {
      alert((err instanceof Error && err.message) ? err.message : 'An error occurred.');
    }
  }

  exportExcel() {
    try {
      this.dataService.exportToExcel(this.importedData);
    } catch (err) {
      alert((err instanceof Error && err.message) ? err.message : 'An error occurred.');
    }
  }

  clearImport(): void {
    this.importedData = [];
    this.previewHeaders = [];
  }
}
