import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PerfilData } from 'src/app/Models/perfilData';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-modal-add-funcionario',
  templateUrl: './modal-add-funcionario.component.html',
  styleUrls: ['./modal-add-funcionario.component.css'],
})
export class ModalAddFuncionarioComponent implements OnInit {
  constructor(private axiosService:AxiosService,private modalService: NgbModal, private eventService:EventsService,  private categoriaService: CategoriaService){}

  ngOnInit(): void {
    this.getPerfis();
  }
  data:PerfilData[] = [];
  cachedResponses: { } = {};
 
  getPerfis(): void {
    this.axiosService.request("GET", "/gerente/funcionariosPerfil", "").then((response) => {
      this.data = response.data;
      this.cachedResponses = response.data;
    });
  }

  nome:string = ''
  cpf:string= ''
  telefone:string = '';
  perfil:number = 0;
  senha:string = "";
  senhaB:string = "";
  load:boolean = false;

  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";

  zerar(){
    this.nome = '';
    this.cpf = '';
    this.perfil = 0;
    this.telefone = '';
    this.senha = '';
    this.senhaB = '';
  }

  onSubmitItem(){
    const ger = 3;
    const gar = 4;
    const caix = 6;
    const coz = 5;
    if(this.verificarCampos()){
      if(this.perfil == ger){
        this.axiosService.request(
          "POST", 
          "/gerente/registrar", 
          {
            nome:this.nome,
            telefone:this.telefone,
            cpf:this.cpf,
            senha:this.senha,
            perfil:{
              id:this.perfil
            }
          }
        ).then((response) => {
          this.mostrarMsg(response.data,1);
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
       }else if(this.perfil == gar){
        this.axiosService.request(
          "POST", 
          "/garcom/registrar", 
          {
            nome:this.nome,
            telefone:this.telefone,
            cpf:this.cpf,
            senha:this.senha,
            perfil:{
              id:this.perfil
            }
          }
        ).then((response) => {
          this.mostrarMsg(response.data,1);
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
       }else if(this.perfil == coz){
        this.axiosService.request(
          "POST", 
          "/cozinha/registrar", 
          {
            login:this.nome,
            senha:this.senha,
            perfil:{
              id:this.perfil
            }
          }
        ).then((response) => {
          this.mostrarMsg(response.data,1);
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
       }else if(this.perfil == caix){
        this.axiosService.request(
          "POST", 
          "/caixa/registrar", 
          {
            login:this.nome,
            senha:this.senha,
            perfil:{
              id:this.perfil
            }
          }
        ).then((response) => {
          this.mostrarMsg(response.data,1);
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
    }
  }

  close() {
    this.modalService.dismissAll();
    this.mostrarErro = false;
    this.zerar();
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

verificarCampos():boolean{
  if(this.nome.trim() == '' || this.cpf.trim() == '' || this.telefone == '' || this.perfil == 0 || this.senha == '' || this.senha != this.senhaB ){
    return false;
  }
  return true;
}
}
