import { Component, Input, OnInit } from '@angular/core';
import { ComandaClienteData } from 'src/app/Models/ComandaData';
  interface Pedido {
    idItem: number;
    nomeItem: string;
    precoItem: number;
    quantidade: number;
    valor: number;
    horario_dataPedido: string;
    horarioPedido: Date;
    status: {
      id: number;
      status: string;
    };
  }
@Component({
  selector: 'app-card-comanda',
  templateUrl: './card-comanda.component.html',
  styleUrls: ['./card-comanda.component.css']
})
export class CardComandaComponent implements OnInit {
  ngOnInit(): void {
    this.verificarStatus();
  }

  confirmarPagamento:boolean = false;
  fecharComanda:boolean = false;
  verificarStatus(){
    if(this.status === "Aguardando Pagamento"){
      this.confirmarPagamento = true;
    }
    if(this.status === "Aberta"){
      this.fecharComanda = true;
    }
  }

@Input()idMesa:number =0;
@Input()valorTotal:number =0;
@Input()status:string ='';
@Input()idComanda:number =0;
@Input()nomeCliente:string = '';
@Input()cpfCliente:string= '';
@Input() pedidos: Pedido[] = [];
@Input() garcom:string = '';
@Input() dataAbertura:string = '';
}
