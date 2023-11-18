import { Component, Input, OnChanges, SimpleChanges, inject } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-model-pedido',
  templateUrl: './model-pedido.component.html',
  styleUrls: ['./model-pedido.component.css']
})
export class ModelPedidoComponent implements OnChanges {
  
  @Input()item:string="";
  @Input()imagem:string="";
  quantidade:number = 1;
  @Input()valor:number|string = "";
  valorTotal:number = 0;
  observacoes:string = "";

 

  add(){
    if(this.quantidade < 10){
      this.quantidade++
      this.calcularValorTotal();
    }
  }
  diminuir(){
    if(this.quantidade > 1){
      this.quantidade --
      this.calcularValorTotal();
    }
  }

  constructor(){}
  ngOnChanges(changes: SimpleChanges): void {
    this.calcularValorTotal();
  }

  calcularValorTotal():void{
    this.valor = typeof this.valor === 'string' ? parseFloat(this.valor) : this.valor as number; // convertendo string para number

    if (!isNaN(this.valor)) {
      this.valorTotal = this.valor * this.quantidade;
      this.valorTotal = parseFloat(this.valorTotal.toFixed(2)); // Limitando para duas casas decimais
    } else {
      console.error('O valor não pode ser convertido para número.');
      this.valorTotal = 0;
    }
  }
}
