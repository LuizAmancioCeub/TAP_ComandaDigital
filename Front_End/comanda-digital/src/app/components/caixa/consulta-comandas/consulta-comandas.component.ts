import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ComandaClienteData, ComandaData } from 'src/app/Models/ComandaData';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { MesaData } from 'src/app/Models/mesaData';
import { AxiosService } from 'src/app/services/axios.service';
import { ComandaService } from 'src/app/services/comanda.service';
import { UserService } from 'src/app/services/user.service';

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
  status: {
    id: number;
    status: string;
  };
}
@Component({
  selector: 'app-consulta-comandas',
  templateUrl: './consulta-comandas.component.html',
  styleUrls: ['./consulta-comandas.component.css']
})
export class ConsultaComandasComponent implements OnInit {
  constructor(private userService:UserService, private axiosService:AxiosService, private modalService:NgbModal, private comandaService:ComandaService){}

  userData:CredencialsData|null = null;
  mesaData:MesaData[] = [];
  
  perfil:number = 0;
  nome:string ="";

  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";
  link:string = "";

  ngOnInit(): void {
    this.initUserData()
  }

  private async initUserData(): Promise<void> {
    this.userData = await this.userService.getUserData();
    if (this.userData !== null) {
      this.recuperarUser();
    } else {
      console.error("Erro ao recuperar dados do usuário");
    }
  }

  private recuperarUser(){
    if(this.userData != null){
     this.perfil = this.userData.perfil.id
     this.nome = this.userData.nome
    }
  }

  garcom: { [id: number]: string } = {}; 
  mesaId:number =0;
  comanda:boolean = false;
  client:boolean=false;
  mesa:boolean = false;
  getMesas():void{
    this.axiosService.request("GET", "/mesas", "").then(
      (response) => {
      this.mesaData = response.data;
      for(const mesa of response.data){
        if(mesa.garcom != null){
          this.garcom[mesa.id] = mesa.garcom.nome
        }
      }
      }
    );
  }

  visualizarClientes(mesaId:number){
    this.mesaId = mesaId;
  }

  close() {
    this.client = false;
    this.comanda = false;
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

  onKeyPress(event: KeyboardEvent) {
    // Obter o código da tecla pressionada
    const charCode = event.which || event.keyCode;

    // Permitir apenas números (códigos de tecla de 0 a 9)
    if (charCode < 48 || charCode > 57) {
      event.preventDefault(); // Impedir a entrada de caracteres não numéricos
    }
  }

  consultaCPF:boolean = true;
  consultaMesa:boolean = false;
  consultaNumero:boolean = false;
  consulta:string='Insira o CPF do cliente'
  consultaBy(num:number){
    if(num === 1){
      this.consultaMesa = false;
      this.consultaNumero = false;
      this.consultaCPF = true;
      this.tipoConsulta = 1;
      this.consulta = 'Insira o CPF do cliente'
    }else if(num === 2){
      this.consultaCPF= false;
      this.consultaNumero = false;
      this.consultaMesa = true;
      this.tipoConsulta = 2;
      this.consulta = 'Insira o número da Mesa'
    }else if( num === 3){
      this.consultaMesa = false;
      this.consultaCPF = false;
      this.consultaNumero = true;
      this.tipoConsulta = 3;
      this.consulta = 'Insira o número da comanda'
    }
  }
tipoConsulta:number = 1;
cpf:string = '';
idMesa:number = 0;
numeroComanda:number = 0;
  pesquisar(num:number){
    if(this.validarCampo()){
      if(this.tipoConsulta === 1){
        this.comandaService.getComandaByCPF(this.cpf)
          .then((response => {
            this.recuperarComandas(response);
          }))
          .catch((error => {
            this.respostaError(error)
          }));
      }else if(this.tipoConsulta === 2){
        this.comandaService.getComandasByMesa(this.idMesa)
          .then((response => {
            this.recuperarComandas(response);
          }))
          .catch((error => {
            this.respostaError(error)
          }));
      }else if(this.tipoConsulta === 3){
        this.comandaService.getComandaByNumero(this.numeroComanda)
          .then((response => {
            this.recuperarComandas(response);
          }))
          .catch((error => {
            this.respostaError(error)
          }));
      }
    }
    
  }

  private validarCampo():boolean{
    console.log(this.idMesa)
    if(this.consultaCPF && (this.cpf.trim() == '' || this.cpf == null || this.cpf.trim().length < 11)){
      this.mostrarMsg("CPF não preenchido",2);
      return false;
    }
    if(this.consultaMesa && this.idMesa == 0 || this.idMesa == null){
      this.mostrarMsg("Número da mesa não preenchido",2);
      return false;
    }
    if(this.consultaNumero && this.numeroComanda == 0 || this.numeroComanda == null){
      this.mostrarMsg("Número da Comanda não preenchido",2);
      return false;
    }
    return true;
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
