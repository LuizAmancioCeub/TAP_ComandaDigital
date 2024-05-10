import { Component, Input, inject, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { ClientesMesaData, MesaData } from 'src/app/Models/mesaData';
import { AxiosService } from 'src/app/services/axios.service';
import { MesaService } from 'src/app/services/mesa.service';
import { FormatarCpfPipe } from 'src/app/Models/Pipers/FormatarPipes';
import { ComandaClienteData, ComandaData } from 'src/app/Models/ComandaData';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-mesa',
  templateUrl: './mesa.component.html',
  styleUrls: ['./mesa.component.css']
})
export class MesaComponent implements OnInit {
  constructor(private axiosService: AxiosService, private mesaService:MesaService, private eventService:EventsService){}
  private modalService = inject(NgbModal);
  modalRef:any;

  ngOnInit():void{
    this.verificarUsuario();
    this.getMesas();

    this.eventService.msgMesa$.subscribe(({ msg, txt }) => {
      this.getMesas()
      this.msg = msg;
      this.txt = txt;
      this.show = "show"
      setTimeout(() => {
        this.show = "";
      }, 2000);
      setTimeout(() => {
        this.msg = false;
      }, 2000);
    });
  }

  dataUser:CredencialsData[] = [];

  perfil:number = 0;
  nomeUser:string='';
  garcom: { [id: number]: string } = {}; 
  comanda:boolean = false;
  client:boolean=false;
 
   verificarUsuario():void{
     this.axiosService.request("GET", "/myCredenciais", "").then(
       (response) => {
         this.dataUser = response.data
         this.nomeUser = response.data.nome;
         const perfil = response.data.perfil.perfil;
       
       if (perfil === "Gerente") {
         this.perfil = 3; 
       }else if (perfil === "Garcom") {
         this.perfil = 4; 
       }else if (perfil === "Caixa") {
        this.perfil = 6; 
      }
     });
   }
  
  data:MesaData[] = [];
  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";
  link:string = "";

  id:number|null=null;
  mesaSelect:any;

  onSubmit(){
    this.onRegister({"id": this.id});
  }

  onRegister(tst: any): void {
    if(!this.id||this.id == null){
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "Todos os campos devem ser preenchidos"
      this.icon = "bi bi-exclamation-triangle-fill";

       // Definir um atraso de 3 segundos para limpar a mensagem de erro
      setTimeout(() => {
        this.mostrarErro = false;
      }, 2000);
    } else{
      this.axiosService.request(
        "POST",
        "/mesa",
        {
          id: tst.id
        }
      ).then(
        (response) => {
          // Lógica de sucesso (se necessário)
          this.getMesas(); // Atualiza a lista de mesas após adicionar uma nova mesa
          this.mostrarErro = true;
          this.alert = "success"
          this.erro = "Mesa cadastrada com Sucesso"
          this.icon = "bi bi-check-circle";
          this.id = null
          // Definir um atraso de 3 segundos para limpar a mensagem de erro
            setTimeout(() => {
            this.mostrarErro = false;
          }, 2000);
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
          // Definir um atraso de 3 segundos para limpar a mensagem de erro
            setTimeout(() => {
            this.mostrarErro = false;
          }, 2000);
    
        }
      );
    }
    
  }

  getMesas():void{
    this.axiosService.request("GET", "/mesas", "").then(
      (response) => {
      this.data = response.data;
      for(const mesa of response.data){
        if(mesa.garcom != null){
          this.garcom[mesa.id] = mesa.garcom.nome
        }
      }
      }
    );
  }

  alterarStatusMesa(id:number, status:number){
    this.axiosService.request("PUT", `mesa/${id}/${status}`,"").then(
      (response) => {
        // Lógica de sucesso (se necessário)
        this.getMesas(); // Atualiza a lista de mesas após adicionar uma nova mesa
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
        // Definir um atraso de 3 segundos para limpar a mensagem de erro
          setTimeout(() => {
          this.mostrarErro = false;
        }, 3000);
  
      }
    )
  }

  excluirMesa:boolean = true;
  
  deletarMesas(id:number):void{
    this.axiosService.request("DELETE","/mesa/"+id, "")
    .then(
      (response) =>{
         // Lógica de sucesso (se necessário)
         this.getMesas(); // Atualiza a lista de mesas após adicionar uma nova mesa
         this.excluirMesa = false;
         this.alert = "success"
         this.erro = "Mesa Excluida com Sucesso"
         this.icon = "bi bi-check-circle";
         // Definir um atraso de 3 segundos para limpar a mensagem de erro
           setTimeout(() => {
            this.modalRef.close('Mesa excluída com sucesso');
         }, 2000);
            setTimeout(() => {
            this.excluirMesa = true;
          }, 2500);
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
        this.excluirMesa = false;
         this.alert = "danger"
         this.icon = "bi bi-exclamation-triangle-fill";
         // Definir um atraso de 3 segundos para limpar a mensagem de erro
         setTimeout(() => {
          this.modalRef.close('Não foi possível excluir Mesa');
       }, 2000);
          setTimeout(() => {
          this.excluirMesa = true;
        }, 2500);
   
       }
    );   
  }
  back: boolean = false;

  editarMesa(id: number): void {
    this.back = !this.back; // Alternar o valor da variável entre true e false
  }

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
  mesaId:number =0;
  visualizarClientes(mesaId:number){
    this.mesaId = mesaId;
  }

  close() {
    this.client = false;
    this.comanda = false;
    this.modalService.dismissAll();
  }

  msg:boolean = false
  txt:string = "";
  show:string=""

}
