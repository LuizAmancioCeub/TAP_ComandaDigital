import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import {LoginComponent} from './pages/login/login.component';
import {CardapioComponent} from './pages/cardapio/cardapio.component';

const routes: Routes = [
  {
    path:'',
    component: LoginComponent
  },
  {
    path:'cardapio',
    component: CardapioComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
