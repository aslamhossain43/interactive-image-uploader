import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UploadFilesComponent } from './components/upload-files/upload-files.component';

const routes: Routes = [
  {path:"",component:UploadFilesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
