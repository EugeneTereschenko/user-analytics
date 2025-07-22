import { Injectable } from '@angular/core';
import * as Papa from 'papaparse';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class DataimportexportService {
  importedData: any[] = [];
  previewHeaders: string[] = [];

  // Parses CSV or Excel file and returns a promise with data and headers
  parseFile(file: File): Promise<{ data: any[], headers: string[] }> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();

      if (file.name.endsWith('.csv')) {
        reader.onload = (e: any) => {
          const csv = e.target.result;
          const result = Papa.parse(csv, { header: true });
          this.importedData = result.data;
          this.previewHeaders = result.meta.fields || [];
          resolve({ data: this.importedData, headers: this.previewHeaders });
        };
        reader.onerror = (error) => reject(error);
        reader.readAsText(file);

      } else if (file.name.endsWith('.xlsx') || file.name.endsWith('.xls')) {
        reader.onload = (e: any) => {
          const data = new Uint8Array(e.target.result);
          const workbook = XLSX.read(data, { type: 'array' });
          const sheetName = workbook.SheetNames[0];
          const worksheet = workbook.Sheets[sheetName];
          this.importedData = XLSX.utils.sheet_to_json(worksheet);
          this.previewHeaders = Object.keys(this.importedData[0] || {});
          resolve({ data: this.importedData, headers: this.previewHeaders });
        };
        reader.onerror = (error) => reject(error);
        reader.readAsArrayBuffer(file);

      } else {
        reject(new Error('Unsupported file format. Please upload CSV or Excel files.'));
      }
    });
  }

  // Export importedData to CSV file
  exportToCSV(data: any[]) {
    if (!data || data.length === 0) {
      throw new Error('No data to export.');
    }
    const csv = Papa.unparse(data);
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8' });
    saveAs(blob, 'export.csv');
  }

  // Export importedData to Excel file
  exportToExcel(data: any[]) {
    if (!data || data.length === 0) {
      throw new Error('No data to export.');
    }
    const worksheet = XLSX.utils.json_to_sheet(data);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Export');
    const wbout = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });

    const blob = new Blob([wbout], { type: 'application/octet-stream' });
    saveAs(blob, 'export.xlsx');
  }
}
