import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpHeaders, HttpEvent, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
type EntityResponseType = HttpResponse<File[]>;
@Injectable({
  providedIn: 'root'
})
export class UploadFileService {

  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  upload(files: FileList): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    for (let x = 0; x < files.length; x++) {
      formData.append('files', files[x]);

    }

    const req = new HttpRequest('POST', `${this.baseUrl}/upload/files`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  find(): Observable<EntityResponseType> {
    return this.http.get<File[]>(`${this.baseUrl}/files`, { observe: 'response' });
  }
}