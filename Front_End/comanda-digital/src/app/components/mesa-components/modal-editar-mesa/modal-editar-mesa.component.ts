import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { GarcomData } from 'src/app/Models/GarcomData';
import { StatusModel } from 'src/app/Models/StatusModel';
import { MesaData } from 'src/app/Models/mesaData';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-modal-editar-mesa',
  templateUrl: './modal-editar-mesa.component.html',
  styleUrls: ['./modal-editar-mesa.component.css']
})
export class ModalEditarMesaComponent implements OnInit {
  constructor(private axiosService:AxiosService,private modalService: NgbModal, private eventService:EventsService,  private categoriaService: CategoriaService){
    this.data = {
      id:0,
      qr_code:'',
      garcom:{
        id:0,
        nome:''
      },
      status:{
        id:0,
        status:''
    }
    }
  }

  @Input()mesaId:number=0;
  garcom:number = 0;
  qr:string='';
  status:number=0;
  ngOnInit(): void {
    this.getMesaId(this.mesaId);
    this.getStatus();
    this.getgarcons();
  }

  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";
  alterarMesa(mesaId:number){
    this.axiosService.request(
      "PUT",
      `/mesa/${mesaId}`,
      {
        id:mesaId,
        qr_code:this.qr,
        garcom:{
          id:this.garcom
        },
        status:{
          id:this.status
        }
      }
    ).then((response) => {
      this.close();
      setTimeout(() => {
        this.eventService.emitMsgMesa(true, "Mesa alterado com Sucesso");
      }, 300);
    }).catch((error) => {
      const responseData = error.response.data;
      if(responseData.fields){
        const errorFields = responseData.fields;
        const fieldName = Object.keys(errorFields)[0];
        const fieldError = errorFields[fieldName];
        this.mostrarMsg(fieldError,2);
      }else{
        const errorDetail = responseData.detail;
        this.mostrarMsg(errorDetail,2);
      }
  });
  }
  data:MesaData;
  getMesaId(mesaId:number){
    this.axiosService.request("GET", `/mesa/${mesaId}`, "").then(
      (response) => {
      this.data = response.data;
      this.status = this.data.status.id
      this.qr = this.data.qr_code
      this.garcom = this.data.garcom?.id
      }
    )
  }

  close() {
    this.modalService.dismissAll();
    this.mostrarErro = false;
  }

  fecharMsg(){
    this.mostrarErro = false;
  }
  mostrarMsg(mensagem:string, tipo:number):void{
    if(tipo == 1){
      this.alert = "success"
      this.erro = mensagem
      this.icon = "bi bi-bookmark-checkl";
    }else if( tipo == 2){
      this.alert = "warning"
      this.erro = mensagem
      this.icon = "bi bi-exclamation-triangle-fill";
     // Definir um atraso de 3 segundos para limpar a mensagem de erro
     setTimeout(() => {
      this.mostrarErro = false;
    }, 3000);
    }
      this.mostrarErro = true;
     
  }

  onKeyPress(event: KeyboardEvent) {
    // Obter o código da tecla pressionada
      const charCode = event.which || event.keyCode;
    // Permitir apenas números (códigos de tecla de 0 a 9)
    if ((charCode < 48 && charCode != 46) || charCode > 57) {
      event.preventDefault(); // Impedir a entrada de caracteres não numéricos
    }
  }

  // função para visualizar/ esconder senha
  public eye():void{
    const inputIcon:any= document.querySelector('.input__iconR')
    const inputPassword:any = document.querySelector('.input__fieldR')
   
    inputIcon.classList.toggle('ri-eye-off-line');
    inputIcon.classList.toggle('ri-eye-line');
    
    inputPassword.type = inputPassword.type === 'password' ? 'text' : 'password'
} 

dataStatus:StatusModel[] = []
getStatus(): void {
    this.axiosService.request("GET", "/status", "").then((response) => {
      for (const status of response.data) {
        if (status.id === 11 || status.id === 12 || status.id === 13 || status.id === 14) {
          this.dataStatus.push(status);
        }
      }
      this.sortCategories(1); // Ofertas selecionado
    });
}

dataGarcom:GarcomData[] = []
getgarcons(): void {
    this.axiosService.request("GET", "/garcom", "").then((response) => {
      this.dataGarcom = response.data;
      this.sortCategories(2); // Ofertas selecionado
    });
}

sortCategories(op:number): void {
  if(op == 1){
    const indexOfCategoria = this.dataStatus.findIndex(
      (status) => status.id === this.status
    );
    if (indexOfCategoria > -1) {
      const cat = this.dataStatus.splice(indexOfCategoria, 1)[0];
      this.dataStatus.unshift({ ...cat,}); // Coloca categoria atual do item no início do array com a propriedade 'active'
    }
  }else if(op == 2){
    const indexOfCategoria = this.dataGarcom.findIndex(
      (g) => g.id == this.garcom
    );
    if (indexOfCategoria > -1) {
      const cat = this.dataGarcom.splice(indexOfCategoria, 1)[0];
      this.dataGarcom.unshift({ ...cat,}); // Coloca categoria atual do item no início do array com a propriedade 'active'
    }
  }
 
}
}
