import { Component, OnInit } from '@angular/core';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { AxiosService } from 'src/app/services/axios.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-caixa',
  templateUrl: './caixa.component.html',
  styleUrls: ['./caixa.component.css']
})
export class CaixaComponent implements OnInit {
  data:CredencialsData|null = null;

  perfil:number = 0;
  nomeUser:string='';
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
      this.userService.setUserData(this.data);
      this.load = false;
    });
  }
}
