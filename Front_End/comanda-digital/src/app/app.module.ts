import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './pages/login/login.component';
import { CardapioComponent } from './pages/cardapio/cardapio.component';
import { FormLoginComponent } from './components/form-login/form-login.component';
import { MenuComponent } from './components/menu/menu.component';
import { CardsComponent } from './components/cardapio-components/cards/cards.component';
import { SliderComponent } from './components/cardapio-components/slider/slider.component';
import { ContentsComponent } from './components/cardapio-components/contents/contents.component';
import { FooterComponent } from './components/footer/footer.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CardapioComponent,
    FormLoginComponent,
    MenuComponent,
    CardsComponent,
    SliderComponent,
    ContentsComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
