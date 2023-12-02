import { Component, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CozinhaService } from 'src/app/services/cozinha.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-modal-entregar',
  templateUrl: './modal-entregar.component.html',
  styleUrls: ['./modal-entregar.component.css']
})
export class ModalEntregarComponent {
  constructor(private modalService:NgbModal, private cozinhaService:CozinhaService, private eventService:EventsService){}
  @Input()idPedido:string|number = ''


  updatePedido(){
    this.cozinhaService.entregarPedido(this.converterParaNumero(this.idPedido),5)
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
