import { Component, Input, TemplateRef, inject } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.css']
})
export class CardsComponent {
  private modalService = inject(NgbModal);
  
  @Input()item:string="";
  @Input()descricao:string="";
  @Input()imagem:string="";
  @Input()preco:number|null|string= null;


  openVerticallyCentered(content: TemplateRef<any>) {
		this.modalService.open(content, { centered: true });
	}
}
