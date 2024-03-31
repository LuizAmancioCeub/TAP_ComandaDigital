import { Component,OnInit } from '@angular/core';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { AxiosService } from 'src/app/services/axios.service';

@Component({
  selector: 'app-comanda',
  templateUrl: './comanda.component.html',
  styleUrls: ['./comanda.component.css']
})
export class ComandaComponent implements OnInit {

  constructor(private axiosService:AxiosService){}
  data:CredencialsData[] = [];

  perfil:number = 0;
  nomeUser:string='';
  mesa:string='';
  ngOnInit(): void {
    this.verificarUsuario();
   }
 
   verificarUsuario():void{
     this.axiosService.request("GET", "/myCredenciais", "").then(
       (response) => {
         this.data = response.data
         this.nomeUser = response.data.nome;
         const perfil = response.data.perfil.perfil;
       
       if (perfil === "Cliente") {
         this.perfil = 1; 
         this.mesa =  response.data.mesa.id;
       } else if (perfil === "Visitante") {
         this.perfil = 2; 
       } else if (perfil === "Gerente") {
         this.perfil = 3; 
       }else if (perfil === "Garcom") {
         this.perfil = 4; 
       }
     });
   }
}
