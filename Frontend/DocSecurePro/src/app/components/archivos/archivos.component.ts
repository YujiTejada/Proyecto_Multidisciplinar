import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FileInfo } from 'src/app/file-info';
import { ApiService } from 'src/app/services/api/api.service';
import { LoginService } from 'src/app/services/login/login.service';



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
  isInFolder: boolean = false;
  constructor(private service: ApiService, private loginService: LoginService, private router: Router) {}

  ngOnInit() {
    // Al iniciar el componente, obtén la lista de archivos en la carpeta actual
    this.getUploadedFiles();
    this.service.onFolderCreated().subscribe(() => {
      // Actualizar la lista de archivos después de crear una carpeta
      this.getUploadedFiles();
    });
    /*this.loginService.checkSession().subscribe(
      (response: string) => {
        if (response === 'user_logged') {
          console.log('Usuario autenticado');
        } else {
          // Usuario no está autenticado
          console.log('Usuario no autenticado');
          this.router.navigate(['/home']);
        }
      },
      (error) => {
        console.error('Error al verificar la sesión', error);
        this.router.navigate(['/home']);
      }
    );*/
  }

  uploadFile() {
    // Método para subir archivos
    const archivoInput = document.getElementById('archivo') as HTMLInputElement;

    if (archivoInput.files && archivoInput.files.length > 0) {
      const formData = new FormData();
      const file = archivoInput.files[0];

      formData.append('file', file, file.name);

      // Llama al servicio para subir el archivo
      this.service.upload(formData, this.currentDirectory).subscribe(
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
      const newPath = this.currentDirectory + folderName + '/';

      this.service.getFilesListInFolder(newPath).subscribe(
        (response: FileInfo[]) => {
          this.uploadedFiles = response.map(file => {
            const isDirectory = !file.name.includes('.');
            return { ...file, isDirectory };
          });
          this.isInFolder = true;
          this.currentDirectory = newPath;
        },
        (error) => {
          console.error('Error al obtener la lista de archivos en la carpeta:', error);
        }
      );
    }
  }


  goBack() {
    if (this.folderHistory.length > 0) {
      const previousFiles = this.folderHistory.pop() || [];
      this.isInFolder = this.folderHistory.length > 0;

      // Obtener la ruta de la carpeta anterior
      const lastSlashIndex = this.currentDirectory.lastIndexOf('/');
      this.currentDirectory = this.currentDirectory.substring(0, lastSlashIndex + 1);

      // Actualizar la propiedad currentDirectory después de obtener la nueva lista de archivos
      if (this.folderHistory.length > 0) {
        const lastFolder = this.folderHistory[this.folderHistory.length - 1];
        this.currentDirectory += lastFolder[lastFolder.length - 1].name + '/';
      } else {
        // Si no hay más historial, la ruta debe ser la raíz
        this.currentDirectory = '/';
      }

      this.uploadedFiles = previousFiles;
      this.getUploadedFiles();
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

  createFolder() {
    const folderName = prompt('Ingrese el nombre de la carpeta:');
    if (folderName) {
      this.service.createFolder(folderName, this.currentDirectory).subscribe(
        (response: any) => {
          if (response.status === 'success') {
            // Actualizar la lista de archivos después de crear una carpeta
            this.getUploadedFiles();
          } else {
            console.error('Error al crear la carpeta:', response.message);
          }
        },
        (error) => {
          console.error('Error al crear la carpeta:', error);
        }
      );
    }
  }

  deleteFolder() {
    const selectedFolder = prompt('Ingrese el nombre de la carpeta a eliminar:');

    if (selectedFolder) {
      this.service.deleteFolder(selectedFolder, this.currentDirectory).subscribe(
        (response: any) => {
          if (response.status === 'success') {
            // Actualizar la lista de archivos después de eliminar la carpeta
            this.getUploadedFiles();
          } else {
            console.error('Error al eliminar la carpeta:', response.message);
          }
        },
        (error) => {
          console.error('Error al eliminar la carpeta:', error);
        }
      );
    }
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
    this.service.deleteFile(filename, this.currentDirectory).subscribe(
      (response: any) => {
        if (response.status === 'success') {
          // Actualiza la lista de archivos después de eliminar el archivo
          this.getUploadedFiles();
        } else {
          console.error('Error al eliminar el archivo:', response.message);
        }
      },
      (error) => {
        console.error('Error al eliminar el archivo:', error);
      }
    );
  }


}
