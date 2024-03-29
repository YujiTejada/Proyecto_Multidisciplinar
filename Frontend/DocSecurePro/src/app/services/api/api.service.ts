import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { FileInfo } from '../../file-info';
import { UserLoginRequest } from 'src/app/models/user/userLoginRequest/user-login-request';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private serverUrl = 'http://localhost:8080';
  private currentDirectory = '/';
  private createFolderSubject = new Subject<void>();
  constructor(private http: HttpClient) { }

  getCurrentDirectory(): string {
    return this.currentDirectory;
  }

  upload(data: any, currentDirectory: string): Observable<any> {
    const uploadUrl = `${this.serverUrl}/upload?currentDirectory=${encodeURIComponent(currentDirectory)}`;

    return this.http.post(uploadUrl, data, {withCredentials: true, responseType: 'text' }).pipe(
      catchError(error => this.handleError(error))
    );
  }  

  getFilesList(currentDirectory: string): Observable<FileInfo[]> {
    const filesUrl = `${this.serverUrl}/files?folderName=${encodeURIComponent(currentDirectory)}`;
    
    return this.http.get<FileInfo[]>(filesUrl, {withCredentials: true}).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getFilesListInFolder(folderName: string): Observable<FileInfo[]> {
    const filesUrl = `${this.serverUrl}/files?folderName=${encodeURIComponent(folderName)}`;
    
    return this.http.get<FileInfo[]>(filesUrl, {withCredentials: true}).pipe(
      catchError(error => this.handleError(error))
    );
  }
    
  downloadFile(filename: string, destinationPath: string): Observable<HttpResponse<Blob>> {
    const downloadUrl = `${this.serverUrl}/download/${filename}?destinationPath=${destinationPath}`;
    
    return this.http.get(downloadUrl, {withCredentials: true, responseType: 'blob', observe: 'response' }).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<HttpResponse<Blob>>;
  }

  deleteFile(filename: string, currentDirectory: string): Observable<any> {
    const deleteUrl = `${this.serverUrl}/delete-file?filename=${encodeURIComponent(filename)}&currentDirectory=${encodeURIComponent(currentDirectory)}`;
    return this.http.delete(deleteUrl, {withCredentials: true}).pipe(
      catchError(error => this.handleError(error))
    );
  }
  
  createFolder(folderName: string, currentDirectory: string): Observable<any> {
    const body = { folderName, currentDirectory };
    return this.http.post(`${this.serverUrl}/create-folder`, body, {withCredentials: true, responseType: 'json' }).pipe(
      catchError(error => this.handleError(error))
    );
  }
  
  // Método para suscribirse a eventos de creación de carpeta
  onFolderCreated(): Observable<void> {
    return this.createFolderSubject.asObservable();
  }

  deleteFolder(folderName: string, currentDirectory: string): Observable<any> {
    const deleteFolderUrl = `${this.serverUrl}/delete-folder?folderName=${encodeURIComponent(folderName)}&currentDirectory=${encodeURIComponent(currentDirectory)}`;
    return this.http.delete(deleteFolderUrl, {withCredentials: true}).pipe(
      tap(() => {
        this.createFolderSubject.next();
      }),
      catchError(error => this.handleError(error))
    );
  }
   
  private handleError(error: any): Observable<never> {
    console.error('Error:', error);
  
    if (error instanceof ErrorEvent) {
      console.error('Error del cliente:', error.error.message);
    } else {
      console.error('Error del servidor:', error.status, error.error);
    }
    return throwError(error);
  }
  
  searchFiles(searchQuery: string, currentDirectory: string): Observable<string[]> {
    const searchUrl = `${this.serverUrl}/search-files?searchQuery=${encodeURIComponent(searchQuery)}&currentDirectory=${encodeURIComponent(currentDirectory)}`;
    
    return this.http.get<string[]>(searchUrl, {withCredentials: true}).pipe(
      catchError(error => this.handleError(error))
    );
  }
  
}
