import { Component, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AtualizaPedidosService } from 'src/app/services/atualiza-pedidos.service';
import { ComandaService } from 'src/app/services/comanda.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-modal-delete',
  templateUrl: './modal-delete.component.html',
  styleUrls: ['./modal-delete.component.css']
})
export class ModalDeleteComponent {
  constructor(private comandaService:ComandaService,private modalService: NgbModal,private atualizaPedidosService:AtualizaPedidosService, private eventService:EventsService ){}
  
  @Input() idPedido:number|string = ""

  onSubmitDelete(){
    if (typeof this.idPedido === "string") {
      const parsedPedido = parseInt(this.idPedido, 10); // O segundo argumento é a base (radix) para a conversão
      
      if ( !isNaN(parsedPedido)) {
        this.idPedido = parsedPedido
        this.comandaService.cancelPedido( this.idPedido, 6)
          .then((response) => {
            // Lidar com a resposta bem-sucedida do servidor (usuário logado)
            this.atualizaPedidosService.emitirAtualizacaoPedido(true);
            this.close();
            setTimeout(() => {
              this.eventService.emitMsgPedido(true, "Pedido Cancelado com Sucesso");
            }, 300);
            // Redirecionar ou fazer outras ações necessárias após o login
          }).catch((error) => {
            console.log(error)
          });
    }
  }
  }

  close() {
    this.modalService.dismissAll();
  }
}
