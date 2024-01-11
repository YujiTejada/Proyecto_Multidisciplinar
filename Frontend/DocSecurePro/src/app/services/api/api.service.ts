import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { FileInfo } from '../../file-info';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private serverUrl = 'http://localhost:8080';
  private currentDirectory = '/';

  constructor(private http: HttpClient) { }

  getCurrentDirectory(): string {
    return this.currentDirectory;
  }

  upload(data: any): Observable<any> {
    const uploadUrl = `${this.serverUrl}/upload`;
    return this.http.post(uploadUrl, data, { responseType: 'text' }).pipe(
      catchError(error => this.handleError(error))
    );
  }  

  getFilesList(): Observable<FileInfo[]> {
    let filesUrl = `${this.serverUrl}/files?folderName=${encodeURIComponent(this.currentDirectory)}`;
  
    return this.http.get<FileInfo[]>(filesUrl).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getFilesListInFolder(folderName: string): Observable<FileInfo[]> {
    const filesUrl = `${this.serverUrl}/files?folderName=${encodeURIComponent(folderName)}`;
    
    return this.http.get<FileInfo[]>(filesUrl).pipe(
      catchError(error => this.handleError(error))
    );
  }
    
  downloadFile(filename: string, destinationPath: string): Observable<HttpResponse<Blob>> {
    const downloadUrl = `${this.serverUrl}/download/${filename}?destinationPath=${destinationPath}`;
    
    return this.http.get(downloadUrl, { responseType: 'blob', observe: 'response' }).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<HttpResponse<Blob>>;
  }

  deleteFile(filename: string): Observable<any> {
    const deleteUrl = `${this.serverUrl}/delete/${filename}`;
    return this.http.delete(deleteUrl).pipe(
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
  
}
