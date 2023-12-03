import { Component, Input, OnChanges } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent {

  constructor(private modalService:NgbModal){}
  @Input()item:string="";
  @Input()itemId:string = "";
  @Input()descricao:string="";
  @Input()imagem:string="";
  @Input()preco:number|null|string= null;

  close() {
    this.modalService.dismissAll();
  }
}
