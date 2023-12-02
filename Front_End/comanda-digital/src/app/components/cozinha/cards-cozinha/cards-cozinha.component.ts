import { Component, OnInit, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PedidosData } from 'src/app/Models/PedidosData';
import { CozinhaService } from 'src/app/services/cozinha.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-cards-cozinha',
  templateUrl: './cards-cozinha.component.html',
  styleUrls: ['./cards-cozinha.component.css']
})
export class CardsCozinhaComponent implements OnInit{

  isPedido:boolean = false;
  
  constructor(private eventService: EventsService,private modalService:NgbModal,private cozinhaService:CozinhaService){}
  ngOnInit(): void {
   this.getPedidos();

    // Ouvir o evento de pedido feito
  this.cozinhaService.pedidoAtt$.subscribe((pedidoAtt: boolean) => {
    console.log("Subscribe")
    if (pedidoAtt) {
      this.getPedidos(); // Atualiza a lista de pedidos
    }
  });

  }

  data:PedidosData[] = [];

  getPedidos(){
    this.cozinhaService.getPedidos(3)
      .then((response) => {
        this.data = response.data;
        if(response.data.horarioPedido != null){
          this.processarHorarios();
        }else{
          response.data.horarioPedido = ''
        }
        
      })
  }
  
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
