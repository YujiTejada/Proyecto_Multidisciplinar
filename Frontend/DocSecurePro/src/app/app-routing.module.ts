import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IndexComponent } from './index/index.component';
import { ArchivosComponent } from './components/archivos/archivos.component';

const routes: Routes = [
  { path: '', component: IndexComponent },
  { path: 'archivos', component: ArchivosComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
