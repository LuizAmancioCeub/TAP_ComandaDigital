import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ComandaClienteData } from 'src/app/Models/ComandaData';
import { ClientesMesaData } from 'src/app/Models/mesaData';
import { AxiosService } from 'src/app/services/axios.service';
import { MesaService } from 'src/app/services/mesa.service';

@Component({
  selector: 'app-modal-clientes',
  templateUrl: './modal-clientes.component.html',
  styleUrls: ['./modal-clientes.component.css']
})
export class ModalClientesComponent implements OnInit {
  
  constructor(private axiosService: AxiosService, private mesaService:MesaService, private modalService:NgbModal){
    this.dataComandaCliente = {
      id:0,
      valorTotal:0,
      status:'',
      cliente:{
          nome:'',
          telefone:'',
          login:''
      },
      mesa:0,
      garcom:'',
      pedidos:[{
        idPedido:0,
          idItem:0,
          nomeItem:'',
          precoItem:0,
          quantidade:0,
          valor:0,
          horarioPedido:'',
          horarioEntrega:'',
          imagem:'',
          observacao:'',
          status:{
              id:0,
              status:''
          }
      }],
      dtAbertura:''
    }
  }

  ngOnInit(): void {
    this.visualizarClientes(this.mesaId);
  }

  @Input()mesaId:number=0;
  comanda:boolean = false;
  client:boolean=false;
  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";
   // Declara um objeto para armazenar as respostas da requisição
   cachedResponses: { [mesaId: number]: any } = {};
   dataClientes:ClientesMesaData[] = [];

   visualizarClientes(mesaId:number){
    if(this.cachedResponses[mesaId]){
      this.dataClientes = this.cachedResponses[mesaId];
      this.mostrarErro = false
      this.client = true;
      this.comanda = false;
    }else{
      this.axiosService.request("GET", `/mesa/${mesaId}/clientes`, "").then(
        (response) => {
          this.mostrarErro = false
          this.client = true;
          this.comanda = false;
          this.cachedResponses[mesaId] = response.data;
          this.dataClientes = response.data;
        },
        (error) => {
          const responseData = error.response.data;
          if(responseData.fields){
            const errorFields = responseData.fields;
            const fieldName = Object.keys(errorFields)[0];
            const fieldError = errorFields[fieldName];
            this.erro = fieldError
          }  else{
            const errorDetail = responseData.detail;
            this.erro = errorDetail
          }
          this.alert = "warning"
          this.icon = "bi bi-exclamation-triangle-fill";
          this.mostrarErro = true;
    
        }
      )
    }
    
  }

  close() {
    this.modalService.dismissAll();
    this.client = false;
    this.comanda = false;
  }

  dataComandaCliente:ComandaClienteData
  verComanda(cpf:string){
    this.axiosService.request("GET", `/consultarComandaAtiva/${cpf}`, "").then(
      (response) => {
        this.mostrarErro = false
        this.client = false;
        this.comanda = true;
      this.dataComandaCliente =response.data;
      },
      (error) => {
        console.log(error)
      }
    );
  }
}
