import { Component, OnInit, TemplateRef } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ComandaClienteData, ComandaData } from 'src/app/Models/ComandaData';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { PedidosData } from 'src/app/Models/PedidosData';
import { AxiosService } from 'src/app/services/axios.service';
import { ComandaService } from 'src/app/services/comanda.service';
import { EventsService } from 'src/app/services/events.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-tabela-comanda',
  templateUrl: './tabela-comanda.component.html',
  styleUrls: ['./tabela-comanda.component.css']
})
export class TabelaComandaComponent implements OnInit {
  constructor(private eventService:EventsService,private comandaService:ComandaService, private modalService:NgbModal, 
              private userService:UserService, private axiosService:AxiosService, private router:Router){}
  dataE:PedidosData[] = [];
  
  pedidos:boolean = false;

  statusFechada:string = "Aguardando Pagamento";
  statusPaga:string = "Paga";
  statusAberta:string = "Aberta";

  load:boolean = true;

  userData:CredencialsData|null = null;
  ngOnInit(): void {
    this.initUserData();
    this.getComanda();
    this.getPedidosEntregues();
  }

  async initUserData(): Promise<void> {
    this.userData = await this.userService.getUserData();
    if (this.userData !== null) {
      this.recuperarUser();
    } else {
      console.error("Erro ao recuperar dados do usuário");
    }
  }
perfil:number = 0;
  recuperarUser(){
    if(this.userData != null){
     this.perfil = this.userData.perfil.id;
    }
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
          console.log("OPAAA")
          this.eventService.existsPedidoEntregue(true);
            
          this.pedidos = true;
        }
        else if(response.data == 0){
          this.eventService.existsPedidoEntregue(false);
          this.pedidos = false;
        }
      }
    )
  }
  dataF: ComandaClienteData | null = null;
  private async getComanda(): Promise<void>{
    this.dataF = await this.comandaService.getComandaData();
    if (this.dataF !== null) {
      this.recuperarComanda();
    } else {
      console.error("Erro ao recuperar dados do usuário");
      this.pedidos = false;
      this.load = false;
    }
  }
valorTotal:number = 0;
idComanda:number = 0;
status:string = "";
  private recuperarComanda(){
    if(this.dataF != null){
      console.log(this.dataF.valorTotal)
      this.valorTotal = this.dataF.valorTotal
      console.log(this.valorTotal)
      this.idComanda = this.dataF.id
      this.status = this.dataF.status
      this.load = false;
    }
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

  openVerticallyCenteredPagamento(content: TemplateRef<any>) {
			this.modalService.open(content, { centered: true,windowClass:'custom' });
	}


  close() {
    this.modalService.dismissAll();
  }

  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";

  newStatusPaga:number = 10;
  realizarPagamento(){
   this.comandaService.updateStatusComanda(this.newStatusPaga)
    .then((response => {
        this.close();
        setTimeout(() => {
          this.eventService.updateComanda();
        }, 300);
      }))
      .catch((error) => {
        const responseData = error.response.data;
      if(responseData.fields){
        const errorFields = responseData.fields;
        const fieldName = Object.keys(errorFields)[0];
        const fieldError = errorFields[fieldName];
        this.mostrarMsg(fieldError);
      }else{
        const errorDetail = responseData.detail;
        this.mostrarMsg(errorDetail);
      }
      });  
  }

  mostrarMsg(mensagem:string):void{
    this.mostrarErro = true;
    this.alert = "warning"
    this.erro = mensagem
    this.icon = "bi bi-exclamation-triangle-fill";
   // Definir um atraso de 3 segundos para limpar a mensagem de erro
   setTimeout(() => {
    this.mostrarErro = false;
  }, 4000);
}
 
}

