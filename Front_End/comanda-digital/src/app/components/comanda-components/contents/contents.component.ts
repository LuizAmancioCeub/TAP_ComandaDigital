import { Component, OnChanges, OnInit, SimpleChanges, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { combineLatest } from 'rxjs';
import { ComandaClienteData, ComandaData } from 'src/app/Models/ComandaData';
import { AxiosService } from 'src/app/services/axios.service';
import { ComandaService } from 'src/app/services/comanda.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-contents-comanda',
  templateUrl: './contents.component.html',
  styleUrls: ['./contents.component.css']
})
export class ContentsComponentComanda implements OnInit, OnChanges {
  constructor(private eventService:EventsService, private axiosService:AxiosService,private modalService:NgbModal, 
              private comandaService:ComandaService){
    
  }
  ngOnChanges(changes: SimpleChanges): void {
   
  }

  msg:boolean = false
  txt:string = "";
  show:string=""
  
  ngOnInit(): void {
    this.getComanda();

    this.eventService.msgPedido$.subscribe(({ msg, txt }) => {
      this.msg = msg;
      this.txt = txt;
      this.show = "show"
      setTimeout(() => {
        this.show = "";
      }, 2000);
      setTimeout(() => {
        this.msg = false;
      }, 3000);
    });

    this.eventService.comanda$.subscribe(({ }) => {
      this.verificarComanda()
    });

  }

  exitsPedidos:boolean = false;
  dataF: ComandaClienteData | null = null;
  private async verificarComanda(): Promise<void>{
    await this.comandaService.verificarComanda();
    this.getComanda();
  }
  private async getComanda(): Promise<void>{
    this.dataF = await this.comandaService.getComandaData();
    if (this.dataF !== null) {
      this.recuperarComanda();
    } else {
      console.error("Erro ao recuperar dados do usuário");
      
    }
  }
valorTotal:number = 0;
idComanda:number = 0;
status:string = "";
statusAberta:string = "Aberta"
statusPaga:string = "Paga"
statusAguaradando:string = "Aguardando Pagamento"
mesa:number = 0;
  private recuperarComanda(){
    if(this.dataF != null){
      this.valorTotal = this.dataF.valorTotal
      this.idComanda = this.dataF.id
      this.status = this.dataF.status
      this.mesa = this.dataF.mesa
      console.log(this.dataF.pedidos.length)
      if(this.dataF.pedidos.length > 0){
        this.exitsPedidos = true;
      }
    }
  }

statusFechar:number = 9;
  fecharComanda(){
    this.axiosService.request(
      "PUT",
      `/minhaComanda/${this.statusFechar}`,
      ""
    ).then((response => {
      this.getAttComanda()
      this.close();
    }));
  }
  statusAbrir:number = 8;
  abrirComanda(){
    this.axiosService.request(
      "PUT",
      `/minhaComanda/${this.statusAbrir}`,
      ""
    ).then((response => {
      this.getAttComanda()
      this.close();
    }));
  }

  async getAttComanda(): Promise<void> {
    const response = await this.axiosService.request("GET", "/minhaComanda", "");
    this.dataF = response.data;
    this.recuperarComanda();
  }

  openVerticallyCentered(content: TemplateRef<any>) {
		this.modalService.open(content, { centered: true,windowClass:'custom' });
	}

  close() {
    this.modalService.dismissAll();
  }
}
