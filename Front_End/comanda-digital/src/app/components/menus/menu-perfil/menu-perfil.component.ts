import { Component } from '@angular/core';
import { ClienteData } from 'src/app/Models/ClienteData';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { AxiosService } from 'src/app/services/axios.service';

@Component({
  selector: 'app-menu-perfil',
  templateUrl: './menu-perfil.component.html',
  styleUrls: ['./menu-perfil.component.css']
})
export class MenuPerfilComponent {
  data:ClienteData[] = [];
  usuario:ClienteData;

  constructor(private axiosService:AxiosService){
    this.usuario = {
      login:"",
      nome:"",
      telefone:"",
      mesa:{
        id:0
      },
      perfil:{
        perfil:""
      }

    }
  }
  ngOnInit(): void {
   this.verificarUsuario();
  }

  verificarUsuario():void{
    this.axiosService.request("GET", "/myCredenciais", "").then(
      (response) => {
        this.usuario = response.data
    });
  }
}
