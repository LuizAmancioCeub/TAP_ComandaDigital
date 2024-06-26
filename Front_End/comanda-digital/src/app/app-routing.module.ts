import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { CardapioComponent } from './pages/cardapio/cardapio.component';
import { ComandaComponent } from './pages/comanda/comanda.component';
import { AuthGuard } from './services/AuthGuard';
import { CozinhaComponent } from './pages/cozinha/cozinha.component';
import { LoadingComponent } from './components/loading/loading.component';
import { GerenteComponent } from './pages/gerente/gerente.component';
import { RoleGuard } from './services/RoleGuard';
import { MesaComponent } from './components/mesa/mesa.component';
import { ManterFuncionariosComponent } from './components/gerente/manter-funcionarios/manter-funcionarios.component';
import { CaixaComponent } from './pages/caixa/caixa.component';
import { MinhasComandasComponent } from './components/comanda-components/minhas-comandas/minhas-comandas.component';

const routes: Routes = [
  {
    path: '',
    component: LoginComponent
  },
  {
    path:'cardapio',
    component: CardapioComponent,
    //canActivate: [AuthGuard] // verifica se usuario pode acessar
    canActivate: [RoleGuard], data: { allowedRoles: [1,2,3] }
  },
  {
    path:'cardapio/comanda',
    component: ComandaComponent,
    canActivate: [RoleGuard], data: { allowedRoles: [1] }
    //canActivate: [AuthGuard]
  },
  {
    path:'gerente',
    component: GerenteComponent,
    canActivate: [RoleGuard], data: { allowedRoles: [3] }
    //canActivate: [AuthGuard]
  },
  {
    path:'cozinha',
    component: CozinhaComponent,
    canActivate: [RoleGuard], data: { allowedRoles: [5, 3] }
    //canActivate: [AuthGuard]
  },
  {
    path:'loading',
    component: LoadingComponent,
    //canActivate: [AuthGuard]
  },
  {
    path:'funcionarios',
    component: ManterFuncionariosComponent,
    canActivate: [RoleGuard], data: { allowedRoles: [3] }
  },
  {
    path:'mesas',
    component: MesaComponent,
    canActivate: [RoleGuard], data: { allowedRoles: [3,4] }
  },
  {
    path:'caixa',
    component: CaixaComponent,
    canActivate: [RoleGuard], data: { allowedRoles: [3,6] }
  },{
    path:'minhasComandas',
    component: MinhasComandasComponent,
    canActivate: [RoleGuard], data: {allowedRoles : [1]}
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
