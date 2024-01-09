import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private serverUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  upload(data: any): Observable<any> {
    const uploadUrl = `${this.serverUrl}/upload`;
    return this.http.post(uploadUrl, data).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getFilesList(): Observable<string[]> {
    const filesUrl = `${this.serverUrl}/files`;
    return this.http.get<string[]>(filesUrl).pipe(
      catchError(error => this.handleError(error))
    );
  }

  downloadFile(filename: string, destinationPath: string): Observable<HttpResponse<Blob>> {
    const downloadUrl = `${this.serverUrl}/download/${filename}?destinationPath=${destinationPath}`;
    
    return this.http.get(downloadUrl, { responseType: 'blob', observe: 'response' }).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<HttpResponse<Blob>>;
  }

  private handleError(error: any): Observable<never> {
    console.error('Error:', error);
    return throwError('Something went wrong. Please try again later.');
  }
}
