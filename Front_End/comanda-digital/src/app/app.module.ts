import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { ComandaComponent } from './pages/comanda/comanda.component';
import { CardapioComponent } from './pages/cardapio/cardapio.component';
import { FormLoginComponent } from './components/form-login/form-login.component';
import { FooterComponent } from './components/footer/footer.component';
import { CardsComponent } from './components/cardapio-components/cards/cards.component';
import { ContentsComponent } from './components/cardapio-components/contents/contents.component';
import { SliderComponent } from './components/cardapio-components/slider/slider.component';
import { TabelaComandaComponent } from './components/comanda-components/tabela-comanda/tabela-comanda.component';
import { TabelaPreparoComponent } from './components/comanda-components/tabela-preparo/tabela-preparo.component';
import { MenuComponent } from './components/menus/menu/menu.component';
import { MenuPerfilComponent } from './components/menus/menu-perfil/menu-perfil.component';
import { ContentsComponentComanda } from './components/comanda-components/contents/contents.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TesteContentComponent } from './testesAPI/teste-content/teste-content.component';
import { FormRegisterComponent } from './components/form-register/form-register.component';
import { ModelPedidoComponent } from './components/cardapio-components/model-pedido/model-pedido.component';
import { LoadingComponent } from './components/loading/loading.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ComandaComponent,
    CardapioComponent,
    FormLoginComponent,
    FooterComponent,
    CardsComponent,
    ContentsComponent,
    SliderComponent,
    TabelaComandaComponent,
    TabelaPreparoComponent,
    ContentsComponentComanda,
    MenuComponent,
    MenuPerfilComponent,
    TesteContentComponent,
    FormRegisterComponent,
    ModelPedidoComponent,
    LoadingComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
