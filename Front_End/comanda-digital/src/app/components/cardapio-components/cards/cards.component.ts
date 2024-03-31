import { Component, Input, OnInit, TemplateRef, inject } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AxiosService } from 'src/app/services/axios.service';

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.css']
})
export class CardsComponent implements OnInit {
  constructor(private axiosService:AxiosService, private modalService:NgbModal){}
 
  
  @Input()item:string="";
  @Input()itemId:number|null = null;
  @Input()descricao:string="";
  @Input()imagem:string="";
  @Input()preco:number|null|string= null;
  @Input()btn:string = "Fazer Pedido";
  @Input()perfil:number=0;
  @Input()categoria:number=0;
  @Input()statusIdItem:number=0;

  statusAtivo:number = 1;
  statusInativo:number = 2;
  isCliente: boolean = false;
  isGerente:boolean = false;
  isVisitante:boolean = false;
  isGarcom:boolean = false;

  ngOnInit(): void {
   this.verificarUsuario()
  }

  verificarUsuario():void{
      if (this.perfil == 1) {
        this.isCliente = true; 

      } else if (this.perfil == 2) {
        this.isVisitante = true; 

      } else if (this.perfil == 3) {
        this.isGerente = true; 

      } else if (this.perfil == 4) {
        this.isGarcom = true; 
      }
  }
/*
  log:boolean = false;
  reg:boolean = true;

  login():void{
    this.reg = false;
    this.log = true;
  }

  registro():void{
    this.log = false;
    this.reg = true;
  }
*/

  openVerticallyCentered(content: TemplateRef<any>) {
		this.modalService.open(content, { centered: true,windowClass:'custom'});
	}
}
