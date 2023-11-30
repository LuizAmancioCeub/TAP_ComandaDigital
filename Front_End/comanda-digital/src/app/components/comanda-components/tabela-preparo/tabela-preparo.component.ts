import { Component, OnDestroy, OnInit, TemplateRef, inject } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { PedidosData } from 'src/app/Models/PedidosData';
import { AtualizaPedidosService } from 'src/app/services/atualiza-pedidos.service';
import { AxiosService } from 'src/app/services/axios.service';
import { ComandaService } from 'src/app/services/comanda.service';

@Component({
  selector: 'app-tabela-preparo',
  templateUrl: './tabela-preparo.component.html',
  styleUrls: ['./tabela-preparo.component.css']
})
export class TabelaPreparoComponent implements OnInit, OnDestroy {
  constructor(private comandaService:ComandaService,private atualizaPedidosService: AtualizaPedidosService){}
  
  data:PedidosData[] = [];
  pedidos:boolean = false;
  private subscription: Subscription| undefined;;

  ngOnInit(): void {
    this.getPedidosEmPreparo();
    this.subscription = this.atualizaPedidosService.atualizacaoPedido$.subscribe((atualizado: boolean) => {
      if (atualizado) {
        this.getPedidosEmPreparo();
      }
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  getPedidosEmPreparo(){
    this.comandaService.getPedidosEmPreparo().then(
      (response) => {
        this.data = response.data;
        this.pedidos = true;
        if(response.data == 0){
          this.pedidos = false;
        }
      }
    );
  }

  private modalService = inject(NgbModal);
  
  openVerticallyCentered(content: TemplateRef<any>) {
		this.modalService.open(content, { centered: true });
	}
}
