import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AtualizaPedidosService } from 'src/app/services/atualiza-pedidos.service';
import { AxiosService } from 'src/app/services/axios.service';
import { ComandaService } from 'src/app/services/comanda.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-modal-update',
  templateUrl: './modal-update.component.html',
  styleUrls: ['./modal-update.component.css']
})
export class ModalUpdateComponent implements OnChanges {
  constructor(private comandaService:ComandaService,private modalService: NgbModal, private eventService:EventsService,private atualizaPedidosService: AtualizaPedidosService){}
  
  ngOnChanges(changes: SimpleChanges): void {
    this.calcularValorTotal();
  }


  @Input() idItem:number|string=""
  @Input() idPedido:number=0
  @Input() item:string=""
  @Input() quantidade:number|string=""
  @Input() observacao:string=""
  @Input() valor:number|string = ""
  valorTotal:number = 0;

  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";
  
 qntd:number = this.quantidade = typeof this.quantidade === 'string' ? parseFloat(this.quantidade) : Number(this.quantidade);

  onSubmitUpdate(){
        this.comandaService.updatePedido( this.idPedido, this.converterParaNumero(this.quantidade), this.valorTotal, this.observacao)
          .then((response) => {
            // Lidar com a resposta bem-sucedida do servidor (usuário logado)
            this.atualizaPedidosService.emitirAtualizacaoPedido(true);
            this.close();
            setTimeout(() => {
              this.eventService.emitMsgPedido(true, "Pedido Atualizado com Sucesso");
            }, 300);
            // Redirecionar ou fazer outras ações necessárias após o login
          }).catch((error) => {
            const responseData = error.response.data;
          if(responseData.fields){
            const errorFields = responseData.fields;
            const fieldName = Object.keys(errorFields)[0];
            const fieldError = errorFields[fieldName];
            this.mostrarMsg(fieldError);
          }else{
            const errorDetail = responseData.detail;
            this.mostrarMsg(errorDetail);
          }
          });
    }

add(){
  this.quantidade = this.converterParaNumero(this.quantidade);
  if(this.quantidade < 10){
    this.quantidade++
    this.calcularValorTotal();
  }
}

diminuir(): void {
  this.quantidade = this.converterParaNumero(this.quantidade);
  if (this.quantidade > 1) {
    this.quantidade -= 1;
    this.calcularValorTotal();
  }
}

calcularValorTotal():void{
  this.valor = typeof this.valor === 'string' ? parseFloat(this.valor) : this.valor as number; // convertendo string para number
  this.quantidade = typeof this.quantidade === 'string' ? parseFloat(this.quantidade) : this.quantidade as number; // convertendo string para number

  if (!isNaN(this.valor)) {
    this.valorTotal = this.valor * this.quantidade;
    this.valorTotal = parseFloat(this.valorTotal.toFixed(2)); // Limitando para duas casas decimais
  } else {
    console.error('O valor não pode ser convertido para número.');
    this.valorTotal = 0;
  }
}

private converterParaNumero(valor: number | string): number {
  return typeof valor === 'string' ? parseFloat(valor) : Number(valor);
}

  close() {
    this.modalService.dismissAll();
  }

  mostrarMsg(mensagem:string):void{
    this.mostrarErro = true;
    this.alert = "warning"
    this.erro = mensagem
    this.icon = "bi bi-exclamation-triangle-fill";
   // Definir um atraso de 3 segundos para limpar a mensagem de erro
   setTimeout(() => {
    this.mostrarErro = false;
  }, 3000);
}
}
