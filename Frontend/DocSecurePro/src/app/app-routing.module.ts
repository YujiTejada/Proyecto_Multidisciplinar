import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IndexComponent } from './components/index/index.component';
import { ArchivosComponent } from './components/archivos/archivos.component';
import { PantallaLoginComponent } from './components/pantalla-login/pantalla-login.component';
import { CorreoComponent } from './components/correo/correo.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: IndexComponent },
  { path: 'archivos', component: ArchivosComponent },
  { path: 'logIn', component:PantallaLoginComponent},
  { path: 'mail', component:CorreoComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
