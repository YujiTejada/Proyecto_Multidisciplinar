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
    this.getUploadedFiles();
    this.service.onFolderCreated().subscribe(() => {
      this.getUploadedFiles();
    });
    this.loginService.checkSession().subscribe(
      (response: string) => {
        if (response === 'user_logged') {
          console.log('Usuario autenticado');
        } else {
          console.log('Usuario no autenticado');
          this.router.navigate(['/home']);
        }
      },
      (error) => {
        console.error('Error al verificar la sesión', error);
        this.router.navigate(['/home']);
      }
    );
  }

  uploadFile() {
    const archivoInput = document.getElementById('archivo') as HTMLInputElement;

    if (archivoInput.files && archivoInput.files.length > 0) {
      const formData = new FormData();
      const file = archivoInput.files[0];

      formData.append('file', file, file.name);

      this.service.upload(formData, this.currentDirectory).subscribe(
        (response: FileInfo[]) => {
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

  openFolder(folder: FileInfo) {
    if (folder.isDirectory) {
      this.folderHistory.push([...this.uploadedFiles]);
      const folderName = folder.name;
      let newPath = '/';
      if (this.currentDirectory != '/') {
        newPath = this.currentDirectory + '/' + folderName;
      } else {
        newPath = this.currentDirectory + folderName;
      }

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
      // Obtener la ruta de la carpeta anterior
      const lastSlashIndex = this.currentDirectory.lastIndexOf('/');
      this.currentDirectory = this.currentDirectory.substring(0, lastSlashIndex);
      // Actualizar la propiedad currentDirectory después de obtener la nueva lista de archivos
      //const lastFolder = this.folderHistory[this.folderHistory.length - 1];
      //this.currentDirectory = lastFolder[lastFolder.length - 1].url || '/';
      this.uploadedFiles = previousFiles;
      this.getUploadedFiles();
    } else {
      this.currentDirectory = '/';
    }
  }  
  
  getUploadedFiles() {
    this.service.getFilesList(this.currentDirectory).subscribe(
      (response: FileInfo[]) => {
        this.uploadedFiles = response.map(file => {
          const isDirectory = !file.name.includes('.');
          return { ...file, isDirectory };
        });
      },
      (error) => {
        console.error('Error al obtener la lista de archivos:', error);
      }
    );
  }

  downloadFile(filename: string) {
    const destinationPath = this.currentDirectory;

    this.service.downloadFile(filename, destinationPath).subscribe(
      (response: any) => {
        this.saveFile(response, filename);
      },
      (error) => {
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

  saveFile(data: any, filename: string) {
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

  searchFiles() {
    this.service.searchFiles(this.searchTerm, this.currentDirectory).subscribe(
      (response: string[]) => {
        console.log('Matching Files:', response);
        
        if (response.length > 0) {
          const folderPath = this.extractFolderPath(response[0]);
          this.loadFolder(folderPath);
        } else {
          console.log('File not found.');
        }
      },
      (error) => {
        console.error('Error searching files:', error);
      }
    );
  }

  private extractFolderPath(filePath: string): string {
    const lastSlashIndex = filePath.lastIndexOf('/');
    return filePath.substring(0, lastSlashIndex + 1);
  }
  
  private loadFolder(folderPath: string) {
    this.service.getFilesList(folderPath).subscribe(
      (response: FileInfo[]) => {
        this.uploadedFiles = response.map(file => {
          const isDirectory = !file.name.includes('.');
          return { ...file, isDirectory };
        });
        this.currentDirectory = folderPath;
      },
      (error) => {
        console.error('Error loading folder:', error);
      }
    );
  }

}
