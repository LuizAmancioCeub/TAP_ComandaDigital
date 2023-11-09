import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { CardapioComponent } from './pages/cardapio/cardapio.component';
import { ComandaComponent } from './pages/comanda/comanda.component';

const routes: Routes = [
  {
    path:'',
    component: LoginComponent
  },
  {
    path:'cardapio',
    component: CardapioComponent
  },
  {
    path:'cardapio/comanda',
    component: ComandaComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
