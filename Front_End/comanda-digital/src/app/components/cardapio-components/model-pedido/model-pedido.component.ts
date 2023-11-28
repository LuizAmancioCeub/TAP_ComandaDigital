import { Component, Input, OnChanges, SimpleChanges, inject } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AxiosService } from 'src/app/services/axios.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-model-pedido',
  templateUrl: './model-pedido.component.html',
  styleUrls: ['./model-pedido.component.css']
})
export class ModelPedidoComponent implements OnChanges {

  constructor(private axiosService:AxiosService,private modalService: NgbModal, private eventService:EventsService){}
  
  @Input()item:string="";
  @Input()imagem:string="";
  @Input()itemId:string|number="";
  quantidade:number = 1;
  @Input()valor:number|string = "";
  valorTotal:number = 0;
  observacoes:string = "";

  msg: boolean = false;

  tst(){
    this.close();
    setTimeout(() => {
      this.eventService.emitMsg(true, "Pedido Realizado com Sucesso");
    }, 300);
  }

  onSubmitPedido(){
    if (typeof this.itemId === "string") {
      const parsedItemId = parseInt(this.itemId, 10); // O segundo argumento é a base (radix) para a conversão
      
      if (!isNaN(parsedItemId)) {
        this.itemId = parsedItemId;

        this.axiosService.request(
          "POST",
          "/pedido",
          {
            cozinha:{
              id: 1
            },
            status:{
              id: 3
            },
            itens:[
              {
                item:{
                  id: this.itemId
                },
                quantidade: this.quantidade,
                observacao: this.observacoes,
                valor: this.valorTotal
              }
            ] 
          }
        ).then((response) => {
          this.close();
          setTimeout(() => {
            this.eventService.emitMsg(true, "Pedido Realizado com Sucesso");
          }, 300);
        }).catch((error) => {
          this.eventService.emitMsg(true, "Não foi possível realizar Pedido");
          console.clear;
        });
      }
    
    }
  }
 

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

  close() {
    this.modalService.dismissAll();
  }
}
