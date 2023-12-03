import { Component, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CozinhaService } from 'src/app/services/cozinha.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-modal-cancelar',
  templateUrl: './modal-cancelar.component.html',
  styleUrls: ['./modal-cancelar.component.css']
})
export class ModalCancelarComponent {
  constructor(private modalService:NgbModal, private cozinhaService:CozinhaService, private eventService:EventsService){}
  @Input()idPedido:string|number = ''


  updatePedido(){
    this.cozinhaService.entregarPedido(this.converterParaNumero(this.idPedido),6)
      .then((response) => {
        this.cozinhaService.emitPedido(true);
        this.close()
      })
  }

  close() {
    this.modalService.dismissAll();
  }

  private converterParaNumero(valor: number | string): number {
    return typeof valor === 'string' ? parseFloat(valor) : Number(valor);
  }
}
