import { Component } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-modal-cancelar',
  templateUrl: './modal-cancelar.component.html',
  styleUrls: ['./modal-cancelar.component.css']
})
export class ModalCancelarComponent {
  constructor(private modalService:NgbModal){}
  close() {
    this.modalService.dismissAll();
  }
}
