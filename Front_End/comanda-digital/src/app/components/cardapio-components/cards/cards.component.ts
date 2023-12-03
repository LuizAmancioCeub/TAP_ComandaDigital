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
  @Input()itemId:string = "";
  @Input()descricao:string="";
  @Input()imagem:string="";
  @Input()preco:number|null|string= null;
  @Input()btn:string = "Fazer Pedido";

  isCliente: boolean = false;

  ngOnInit(): void {
   this.verificarUsuario()
  }

  verificarUsuario():void{
    this.axiosService.request("GET", "/myCredenciais", "").then(
      (response) => {
      const perfil = response.data.perfil.perfil;
      
      if (perfil === "Cliente") {
        this.isCliente = true; 

      } else if (perfil === "Visitante") {
        this.isCliente = false; 

      } else {
        this.isCliente = false; 
      }
      }
    );
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
		this.modalService.open(content, { centered: true });
	}
}
