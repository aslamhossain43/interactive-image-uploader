import { Component, OnInit } from '@angular/core';
import { UploadFileService } from 'src/app/services/upload-file.service';
import { HttpResponse } from '@angular/common/http';
import { WebSocketAPI } from './websocket';
@Component({
  selector: 'app-upload-files',
  templateUrl: './upload-files.component.html',
  styleUrls: ['./upload-files.component.scss']
})
export class UploadFilesComponent implements OnInit {
  selectedFiles: FileList;
  currentFile: File;
  files: File[];
  webSocketAPI: WebSocketAPI;
  notification: string;
  isNotification = false;
  constructor(private uploadService: UploadFileService) {
    this.webSocketAPI = new WebSocketAPI(this);
  }
  ngOnInit(): void {
    this.loadPage();
    this.webSocketAPI._connect();
  }

  upload(): void {
    this.uploadService.upload(this.selectedFiles).subscribe(() => {
      this.loadPage();
      this.webSocketAPI._send();
    },
      () => { });
  }
  selectFile(event) {
    this.selectedFiles = event.target.files;
  }
  loadPage(): void {
    this.uploadService.find()
      .subscribe(
        (res: HttpResponse<File[]>) => {
          this.files = res.body;
        },
        () => { }
      );
  }

  handleMessage(response: string) {
    this.isNotification = true;
    this.notification = response;
  }
  closeNotification() {
    this.isNotification = false;
    this.notification = '';
  }
}
