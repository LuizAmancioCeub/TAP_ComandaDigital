import { Component, OnInit } from '@angular/core';
import { ComandaData } from 'src/app/Models/ComandaData';
import { PedidosData } from 'src/app/Models/PedidosData';
import { AxiosService } from 'src/app/services/axios.service';
import { ComandaService } from 'src/app/services/comanda.service';

@Component({
  selector: 'app-tabela-comanda',
  templateUrl: './tabela-comanda.component.html',
  styleUrls: ['./tabela-comanda.component.css']
})
export class TabelaComandaComponent implements OnInit {
  constructor(private comandaService:ComandaService){
    this.dataF = {
      id: 0,
      valorTotal: 0,
      cliente:{
        login:"",
        nome:""
      },
      mesa:{
        id:0
      },
      status:{
        id:0,
        status:""
      }
    }
  }
  dataE:PedidosData[] = [];
  
  pedidos:boolean = false;

  ngOnInit(): void {
    this.getPedidosEntregues();
    this.getComanda();
  }

  getPedidosEntregues(){
    this.comandaService.getPedidosEntregues().then(
      (response) => {
        this.dataE = response.data;
        this.pedidos = true;
        if(response.data == 0){
          this.pedidos = false;
        }
      }
    );
  }
  dataF: ComandaData;
  getComanda(){
    this.comandaService.getComanda().then(
      (response) => {
        this.dataF = response.data;
      }
    );
  }
}

