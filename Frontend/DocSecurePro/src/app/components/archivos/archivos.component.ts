import { Component, OnInit } from '@angular/core';
import { FileInfo } from 'src/app/file-info';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-archivos',
  templateUrl: './archivos.component.html',
  styleUrls: ['./archivos.component.css']
})
export class ArchivosComponent implements OnInit {
  file: File | null = null;
  uploadedFiles: FileInfo[] = [];
  folderHistory: FileInfo[][] = [];
  searchTerm: string = '';
  currentDirectory: string = '/';

  constructor(private service: ApiService) {}

  ngOnInit() {
    // Al iniciar el componente, obtén la lista de archivos en la carpeta actual
    this.getUploadedFiles();
  }

  uploadFile() {
    // Método para subir archivos
    const archivoInput = document.getElementById('archivo') as HTMLInputElement;

    if (archivoInput.files && archivoInput.files.length > 0) {
      const formData = new FormData();
      const file = archivoInput.files[0];

      formData.append('file', file, file.name);

      // Llama al servicio para subir el archivo
      this.service.upload(formData).subscribe(
        (response: FileInfo[]) => {
          // Actualiza la lista de archivos después de subir uno nuevo
          this.getUploadedFiles();
        },
        (error) => {
          // Manejo de errores al subir el archivo
          console.error('Error al subir el archivo:', error);
          // Agregar un manejo específico del error para obtener más detalles
          if (error.error instanceof ErrorEvent) {
            console.error('Error del cliente:', error.error.message);
          } else {
            console.error('Error del servidor:', error.status, error.error);
          }
        }
      );
    } else {
      console.error('No se ha seleccionado ningún archivo.');
    }
  }

  openFolder(folder: FileInfo) {
    if (folder.isDirectory) {
      this.folderHistory.push([...this.uploadedFiles]);
      const folderName = folder.name;
      this.currentDirectory = `${this.currentDirectory}${folderName}/`;
      this.service.getFilesListInFolder(folderName).subscribe(
        (response: FileInfo[]) => {
          this.uploadedFiles = response.map(file => {
            const isDirectory = !file.name.includes('.');
            return { ...file, isDirectory };
          });
        },
        (error) => {
          console.error('Error al obtener la lista de archivos en la carpeta:', error);
        }
      );
    }
  }
  
  goBack() {
    // Método para retroceder en la navegación de carpetas
    if (this.folderHistory.length > 0) {
      // Si hay historial, retrocede y actualiza la lista de archivos
      this.uploadedFiles = this.folderHistory.pop() || [];
    }
  }

  getUploadedFiles() {
    // Método para obtener la lista de archivos en la carpeta actual
    this.service.getFilesList().subscribe(
      (response: FileInfo[]) => {
        // Actualiza la lista de archivos
        this.uploadedFiles = response.map(file => {
          const isDirectory = !file.name.includes('.');
          return { ...file, isDirectory };
        });
      },
      (error) => {
        // Manejo de errores al obtener la lista de archivos
        console.error('Error al obtener la lista de archivos:', error);
      }
    );
  }

  downloadFile(filename: string) {
    // Método para descargar un archivo
    const destinationPath = '';
  
    // Llama al servicio para descargar el archivo
    this.service.downloadFile(filename, destinationPath).subscribe(
      (response: any) => {
        // Guarda el archivo descargado
        this.saveFile(response, filename);
      },
      (error) => {
        // Manejo de errores al descargar el archivo
        console.error('Error al descargar el archivo:', error);
      }
    );
  }
  
  private saveFile(data: any, filename: string) {
    // Método para guardar el archivo descargado
    const blob = new Blob([data.body], { type: 'application/octet-stream' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    document.body.appendChild(a);
    a.style.display = 'none';
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
  }

  deleteFile(filename: string) {
    // Método para eliminar un archivo
    this.service.deleteFile(filename).subscribe(
      (response) => {
        // Si el archivo se elimina correctamente, actualiza la lista de archivos
        console.log('Archivo eliminado exitosamente:', response);
        this.getUploadedFiles();
      },
      (error) => {
        // Manejo de errores al eliminar el archivo
        console.error('Error al eliminar el archivo:', error);
        // Agregar un manejo específico del error para obtener más detalles
        if (error.error instanceof ErrorEvent) {
          console.error('Error del cliente:', error.error.message);
        } else {
          console.error('Error del servidor:', error.status, error.error);
        }
      }
    );
  }
}
