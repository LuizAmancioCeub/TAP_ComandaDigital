import { Component, OnInit } from '@angular/core';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { AxiosService } from 'src/app/services/axios.service';
import { LoaderService } from 'src/app/services/loader.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-cardapio',
  templateUrl: './cardapio.component.html',
  styleUrls: ['./cardapio.component.css']
})
export class CardapioComponent implements OnInit{
  data:CredencialsData|null = null;

  perfil:number = 0;
  nomeUser:string='';
  mesa:string='';
  load:boolean = true;
  

  constructor(private axiosService:AxiosService, private userService:UserService){}
  ngOnInit(): void {
   this.verificarUsuario();
   
  }

  verificarUsuario():void{
    this.axiosService.request("GET", "/myCredenciais", "").then(
      (response) => {
        this.data = response.data
        this.nomeUser = response.data.nome;
        this.perfil = response.data.perfil.id;
      if (this.perfil == 1) {
        this.mesa =  response.data.mesa.numero;
      }
      this.userService.setUserData(this.data);
      this.load = false;
    });
  }
}
