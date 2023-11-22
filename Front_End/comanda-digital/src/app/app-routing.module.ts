import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { CardapioComponent } from './pages/cardapio/cardapio.component';
import { ComandaComponent } from './pages/comanda/comanda.component';
import { TesteContentComponent } from './testesAPI/teste-content/teste-content.component';
import { AuthGuard } from './services/AuthGuard';

const routes: Routes = [
  {
    path:'',
    component: LoginComponent
  },
  {
    path:'cardapio',
    component: CardapioComponent,
    //canActivate: [AuthGuard] // verifica se usuario pode acessar
  },
  {
    path:'cardapio/comanda',
    component: ComandaComponent,
   // canActivate: [AuthGuard]
  },
  {
    path:'teste',
    component: TesteContentComponent,
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
