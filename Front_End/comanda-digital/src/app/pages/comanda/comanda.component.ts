import { Conditional } from '@angular/compiler';
import { Component,OnInit } from '@angular/core';
import { ComandaClienteData, ComandaData } from 'src/app/Models/ComandaData';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { AxiosService } from 'src/app/services/axios.service';
import { ComandaService } from 'src/app/services/comanda.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-comanda',
  templateUrl: './comanda.component.html',
  styleUrls: ['./comanda.component.css']
})
export class ComandaComponent implements OnInit {

  constructor(private axiosService:AxiosService, private userService:UserService, private comandaService:ComandaService){}
  userData:CredencialsData|null = null;

  perfil:number = 0;
  nomeUser:string='';
  mesa:string='';
  ngOnInit(): void {
    this.initUserData();
   }

   async initUserData(): Promise<void> {
    this.userData = await this.userService.getUserData();
    if (this.userData !== null) {
      this.verificarUsuario();
    } else {
      console.error("Erro ao recuperar dados do usuário");
    }
  }
 
   verificarUsuario():void{
    if(this.userData != null){
        this.nomeUser =  this.userData.nome;
         const perfil = this.userData.perfil.perfil;
       
       if (perfil === "Cliente") {
         this.perfil = 1; 
         this.mesa =  this.userData.mesa.numero.toString();
       } else if (perfil === "Visitante") {
         this.perfil = 2; 
       } else if (perfil === "Gerente") {
         this.perfil = 3; 
       }else if (perfil === "Garcom") {
         this.perfil = 4; 
       }
    }  
   }
}
