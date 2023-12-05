import { Component, OnInit, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ComandaData } from 'src/app/Models/ComandaData';
import { PedidosData } from 'src/app/Models/PedidosData';
import { AxiosService } from 'src/app/services/axios.service';
import { ComandaService } from 'src/app/services/comanda.service';

@Component({
  selector: 'app-tabela-comanda',
  templateUrl: './tabela-comanda.component.html',
  styleUrls: ['./tabela-comanda.component.css']
})
export class TabelaComandaComponent implements OnInit {
  constructor(private comandaService:ComandaService, private modalService:NgbModal){
    this.dataF = {
      id: 0,
      valorTotal: 0,
      cliente:{
        login:"",
        nome:""
      },
      mesa:{
        id:0
      },
      status:{
        id:0,
        status:""
      }
    }
  }
  dataE:PedidosData[] = [];
  
  pedidos:boolean = false;

  load:boolean = true;

  ngOnInit(): void {
    this.getPedidosEntregues();
    this.getComanda();
  }

  getPedidosEntregues(){
    this.comandaService.getPedidosEntregues().then(
      (response) => {
        if(response.data != 0){
          this.dataE = response.data;
          if(response.data.horarioEntrega != null){
            this.processarHorarios();
          }else{
            response.data.horarioEntrega = ''
          }
          this.pedidos = true;
        }
        else if(response.data == 0){
          this.pedidos = false;
        }
      }
    );
  }
  dataF: ComandaData;
  getComanda(){
    this.comandaService.getComanda().then(
      (response) => {
        this.dataF = response.data;
        this.load = false;
      }
    );
  }

  processarHorarios(): void {
    // Iterar pelos pedidos e converter a string de horário para um objeto Date
    
    this.dataE.forEach((pedido) => {
      pedido.horarioEntrega = new Date(pedido.horario_dataEntrega); 
       // Obtendo horas e minutos do horário
    const horas = pedido.horarioEntrega.getHours();
    const minutos = pedido.horarioEntrega.getMinutes();

    // Formatar para exibir conforme necessário (por exemplo, no formato HH:MM)
    const horarioFormatado = `${horas.toString().padStart(2, '0')}:${minutos.toString().padStart(2, '0')}`;

    // Armazenar a string formatada de horas e minutos de volta na propriedade
    pedido.horario_dataEntrega = horarioFormatado;
    });
  }

  openVerticallyCentered(content: TemplateRef<any>) {
		this.modalService.open(content, { centered: true,windowClass: 'custom-modal-comanda-item'});
	}

  close() {
    this.modalService.dismissAll();
  }
}

