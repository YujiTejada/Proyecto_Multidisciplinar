// archivos.component.ts

import { Component, OnInit } from '@angular/core';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-archivos',
  templateUrl: './archivos.component.html',
  styleUrls: ['./archivos.component.css']
})
export class ArchivosComponent implements OnInit {
  file: File | null = null;
  uploadedFiles: any[] = [];

  constructor(private service:ApiService) {}

  ngOnInit() {
    this.getUploadedFiles();
  }

  uploadFile() {
    const archivoInput = document.getElementById('archivo') as HTMLInputElement;
  
    if (archivoInput.files && archivoInput.files.length > 0) {
      const formData = new FormData();
      const file = archivoInput.files[0];
  
      formData.append('file', file, file.name);
  
      this.service.upload(formData).subscribe(
        (response: any) => {
          this.uploadedFiles = response;
          this.getUploadedFiles();
        },
        (error) => {
          console.error('Error al subir el archivo:', error);
        }
      );
    } else {
      console.error('No se ha seleccionado ningún archivo.');
    }
  }
  

  getUploadedFiles() {
    this.service.getFilesList().subscribe((response: any) => {
      this.uploadedFiles = response;
    });
  }

  downloadFile(filename: string) {
    const destinationPath = '';
  
    this.service.downloadFile(filename, destinationPath).subscribe(
      (response: any) => {
        const blob = new Blob([response.body], { type: 'application/octet-stream' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        document.body.appendChild(a);
        a.style.display = 'none';
        a.href = url;
        a.download = filename;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      (error) => {
        console.error('Error downloading file:', error);
      }
    );
  }

}
