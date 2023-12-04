import { Component, OnInit } from '@angular/core';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { AxiosService } from 'src/app/services/axios.service';
import { LoaderService } from 'src/app/services/loader.service';

@Component({
  selector: 'app-cardapio',
  templateUrl: './cardapio.component.html',
  styleUrls: ['./cardapio.component.css']
})
export class CardapioComponent implements OnInit{
  data:CredencialsData[] = [];

  constructor(private axiosService:AxiosService){}
  ngOnInit(): void {
   this.verificarUsuario();
   
  }

  verificarUsuario():void{
    this.axiosService.request("GET", "/myCredenciais", "").then(
      (response) => {
        this.data = response.data
    });
  }
}
