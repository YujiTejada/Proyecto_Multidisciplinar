import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IndexComponent } from './components/index/index.component';
import { ArchivosComponent } from './components/archivos/archivos.component';
import { PantallaLoginComponent } from './components/pantalla-login/pantalla-login.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' }, // Redirigir de la ruta vacía a /home
  { path: 'home', component: IndexComponent },
  { path: 'archivos', component: ArchivosComponent },
  { path: 'logIn', component:PantallaLoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
