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
        if(response.data.horarioPedido != null){
          this.processarHorarios();
        }else{
          response.data.horarioPedido = ''
        }
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

  processarHorarios(): void {
    // Iterar pelos pedidos e converter a string de horário para um objeto Date
    
    this.data.forEach((pedido) => {
      pedido.horarioPedido = new Date(pedido.horario_dataPedido); 
       // Obtendo horas e minutos do horário
    const horas = pedido.horarioPedido.getHours();
    const minutos = pedido.horarioPedido.getMinutes();

    // Formatar para exibir conforme necessário (por exemplo, no formato HH:MM)
    const horarioFormatado = `${horas.toString().padStart(2, '0')}:${minutos.toString().padStart(2, '0')}`;

    // Armazenar a string formatada de horas e minutos de volta na propriedade
    pedido.horario_dataPedido = horarioFormatado;
    });
  }
}
