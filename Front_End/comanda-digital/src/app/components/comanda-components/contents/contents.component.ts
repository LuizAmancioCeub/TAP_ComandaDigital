import { Component, OnChanges, OnInit, SimpleChanges, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription, combineLatest } from 'rxjs';
import { ComandaClienteData, ComandaData } from 'src/app/Models/ComandaData';
import { AtualizaPedidosService } from 'src/app/services/atualiza-pedidos.service';
import { AxiosService } from 'src/app/services/axios.service';
import { ComandaService } from 'src/app/services/comanda.service';
import { EventsService } from 'src/app/services/events.service';

interface Pedido {
  idPedido:number,
  idItem: number;
  nomeItem: string;
  precoItem: number;
  quantidade: number;
  valor: number;
  horarioPedido: string;
  horarioEntrega:string;
  imagem:string;
  observacao:string;
  status: {
    id: number;
    status: string;
  };
}

@Component({
  selector: 'app-contents-comanda',
  templateUrl: './contents.component.html',
  styleUrls: ['./contents.component.css']
})
export class ContentsComponentComanda implements OnInit, OnChanges {
  constructor(private eventService:EventsService, private axiosService:AxiosService,private modalService:NgbModal, 
              private comandaService:ComandaService, private atualizaPedidosService: AtualizaPedidosService){
    
  }
  ngOnChanges(changes: SimpleChanges): void {
   
  }

  msg:boolean = false
  txt:string = "";
  show:string=""
  
  private subscription: Subscription| undefined;
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

    this.subscription = this.atualizaPedidosService.atualizacaoPedido$.subscribe((atualizado: boolean) => {
      if (atualizado) {
        this.verificarComanda();
      }
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
      this.recuperarPedidos();
    }
  }

pedidosEntregues:Pedido[] = [];
pedidosEmPreparo:Pedido[] = [];
pedidosCancelados:Pedido[] = [];
private recuperarPedidos(){
  this.dataF?.pedidos.forEach(pedido => {
    if (pedido.status.id === 5) {
      this.pedidosEntregues.push(pedido);
    } else if (pedido.status.id === 3) {
      this.pedidosEmPreparo.push(pedido);
    } else if (pedido.status.id === 6) {
      this.pedidosCancelados.push(pedido);
    }
  });
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
