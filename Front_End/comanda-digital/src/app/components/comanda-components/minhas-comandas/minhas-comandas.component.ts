import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ComandaClienteData } from 'src/app/Models/ComandaData';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { AxiosService } from 'src/app/services/axios.service';
import { ComandaService } from 'src/app/services/comanda.service';
import { UserService } from 'src/app/services/user.service';

interface Pedido {
  idItem: number;
  nomeItem: string;
  precoItem: number;
  quantidade: number;
  valor: number;
  horarioPedido: string;
  horarioEntrega:string;
  imagem:string;
  status: {
    id: number;
    status: string;
  };
}
@Component({
  selector: 'app-minhas-comandas',
  templateUrl: './minhas-comandas.component.html',
  styleUrls: ['./minhas-comandas.component.css']
})
export class MinhasComandasComponent implements OnInit {

  constructor(private userService:UserService, private axiosService:AxiosService, private modalService:NgbModal, private comandaService:ComandaService){}
  
  userData:CredencialsData|null = null;
  load:boolean = true;
  perfil:number = 0;
  nome:string ="";

  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";
  link:string = "";

  ngOnInit(): void {
    this.initUserData();
    this.getMyComandas();
  }

  private async initUserData(): Promise<void> {
    this.userData = await this.userService.getUserData();
    if (this.userData !== null) {
      this.recuperarUser();
    } else {
      console.error("Erro ao recuperar dados do usuário");
    }
  }

  mesa:string = "";
  private recuperarUser(){
    if(this.userData != null){
     this.perfil = this.userData.perfil.id
     this.nome = this.userData.nome
     this.mesa = this.userData.mesa.numero.toString()
    }
  }

  private getMyComandas(){
    this.comandaService.getMyComandas()
          .then((response => {
            this.recuperarComandas(response);
            this.load = false;
          }))
          .catch((error => {
            this.respostaError(error);
            this.load = false;
          }));
  }

  garcom: { [id: number]: string } = {}; 
  mesaId:number =0;

  visualizarClientes(mesaId:number){
    this.mesaId = mesaId;
  }

  close() {
    this.modalService.dismissAll();
  }

  modalRef:any;
  mesaSelect:any;
  openVerticallyCentered(content: any, mesa:any) {
    this.mesaSelect = mesa;
		this.modalRef = this.modalService.open(content, { centered: true,windowClass:'customMesa' });
	}
  openVerticallyCenteredEdit(content: any, mesa:any) {
    this.mesaId = mesa;
		this.modalRef = this.modalService.open(content, { centered: true, windowClass:'custom' });
	}

  openVerticallyCenteredLG(content: any, mesa:any) {
    this.mesaId = mesa;
		this.modalRef = this.modalService.open(content, { centered: true,size:'lg',windowClass:'customMesa' });
	}

  comandaData:ComandaClienteData[] = []
  recuperarComandas(response:any){
    if (Array.isArray(response.data)) {
      // Se for uma array, atribui diretamente
      this.comandaData = response.data;
    } else {
      // Se for um objeto, coloca dentro de uma array
      this.comandaData = [response.data];
    }
  }
  
  private respostaError(msg:any):void{
   
      const responseData = msg.response.data;
      if(responseData.fields){
        const errorFields = responseData.fields;
        const fieldName = Object.keys(errorFields)[0];
        const fieldError = errorFields[fieldName];
        this.mostrarMsg(fieldError,2);
      }else{
        const errorDetail = responseData.detail;
        this.mostrarMsg(errorDetail,2);
      }
  }

  private mostrarMsg(mensagem:string, tipo:number):void{
    this.mostrarErro = false;
    if(tipo == 1){
      this.alert = "success"
      this.erro = mensagem
      this.icon = "bi bi-bookmark-checkl";
    }else if( tipo == 2){
      this.alert = "warning"
      this.erro = mensagem
      this.icon = "bi bi-exclamation-triangle-fill";
    }
    setTimeout(() => {
      this.mostrarErro = false;
    }, 3000);

      this.mostrarErro = true;
  }
statusCancelado:number = 6;
pedidos: Pedido[] = [];
  private pedidosNaoCancelados(comanda:ComandaClienteData){
    for(let p of comanda.pedidos){
      if(p.status.id != this.statusCancelado){
        this.pedidos.push(p);
      }
    }
  }
}
