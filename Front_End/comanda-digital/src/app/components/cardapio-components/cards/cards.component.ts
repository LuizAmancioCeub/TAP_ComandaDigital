import { Component, Input, OnInit, TemplateRef, inject } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ComandaClienteData } from 'src/app/Models/ComandaData';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';
import { ComandaService } from 'src/app/services/comanda.service';
import { EventsService } from 'src/app/services/events.service';
import { GerenteService } from 'src/app/services/gerente.service';

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.css']
})
export class CardsComponent implements OnInit {
  constructor(private axiosService:AxiosService, private modalService:NgbModal,private gerenteservice:GerenteService, 
    private eventService:EventsService,private categoriaService:CategoriaService, private comandaService:ComandaService){}
 
  
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
  desativado:boolean = false;
  excluirItem:boolean = false;

  ngOnInit(): void {
   this.verificarUsuario();
   this.verificarStatusItem();
   this.verificarComanda();
  }

  private verificarUsuario():void{
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

  private verificarStatusItem():void{
    if(this.statusIdItem == 2){
      this.desativado = true;
    }
  }

  onSubmitAtivar(){
    this.gerenteservice.updateStatusItem(this.itemId, this.statusAtivo)
      .then((response) => {
        this.categoriaService.setCategoriaSelecionada(this.categoria);
        setTimeout(() => {
          this.eventService.emitMsg(true, "Item ativado com Sucesso");
        }, 100);
      }).catch((error) => {
        const errorDetail = error.response.data.detail;
        this.eventService.emitMsg(true, errorDetail);
        console.clear;
      });
  }

  excluiItem(){
    this.excluirItem = true;
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

  statusFechada:string = "Aguardando Pagamento"
  statusComanda:string = "";
  comandadata:ComandaClienteData | null = null;
  private async verificarComanda(): Promise<void>{
    this.comandadata = await this.comandaService.getComandaData();
    if(this.comandadata !== null){
      this.recuperarComanda();
    }
  }

  private recuperarComanda(){
    if(this.comandadata !== null){
     this.statusComanda = this.comandadata.status
    }
  }
}
