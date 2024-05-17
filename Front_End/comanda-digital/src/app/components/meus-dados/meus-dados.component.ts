import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { AxiosService } from 'src/app/services/axios.service';
import { UserService } from 'src/app/services/user.service';
import jsQR from 'jsqr';
import { MesaService } from 'src/app/services/mesa.service';
import { MesaData } from 'src/app/Models/mesaData';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-meus-dados',
  templateUrl: './meus-dados.component.html',
  styleUrls: ['./meus-dados.component.css']
})
export class MeusDadosComponent implements OnInit {

  constructor(private userService:UserService, private axiosService:AxiosService, public mesaService:MesaService, private modalService:NgbModal){}
  @ViewChild('videoElement') videoElement!: ElementRef;
  userData:CredencialsData|null = null;
  perfil:number = 0;
  descPerfil:string = '';
  cpf:string = ''
  cpfValidador:string = '';
  nome:string = ''
  telefone:string=''
  email:string= ''
  mesaNum:number=0
  mesaGarcom:string=''
  matricula:string = ''
  matriculaValidador:string = ''

  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";

  ngOnInit(): void {
    this.initUserData();
  }

  private async initUserData(): Promise<void> {
    this.userData = await this.userService.getUserData();
    if (this.userData !== null) {
      this.recuperarUser();
    } else {
      console.error("Erro ao recuperar dados do usuário");
    }
  }
  close() {
    this.modalService.dismissAll();
    this.mostrarErro = false;
    this.mesaService.alterarMesa = false;
    this.mesaService.camera = false;
  }
  private recuperarUser(){
    if(this.userData != null){
      this.perfil = this.userData.perfil.id
      this.descPerfil = this.userData.perfil.perfil;
      this.cpf = this.userData.cpf
      this.cpfValidador = this.userData.cpf
      this.matriculaValidador = this.userData.login
      this.nome = this.userData.nome
      this.telefone = this.userData.telefone
      this.email = this.userData.email
      if(this.userData.perfil.id == 1){
        this.mesaNum = this.userData.mesa.numero
        this.mesaGarcom = this.userData.mesa.garcom
      }
      this.matricula = this.userData.login
    }
  }


  private mostrarMsg(mensagem:string, tipo:number):void{
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

  fecharMsg(){
    this.mostrarErro = false;
  }

  alterarDados(){
    if(this.cpfValidador === this.cpf){
      if(this.perfil == 1){
        this.userService.alterarDadosCliente(this.cpf, this.nome, this.telefone, this.email, this.perfil)
          .then((response => {
            this.resposta(1, response)
            this.preencher(this.cpf, this.nome, this.telefone, this.email, this.perfil);
          }))
          .catch((error => {
            this.resposta(2,error)
          }));
      } 
      else if( this.perfil == 3){
        this.userService.alterarDadosGerente(this.matricula, this.cpf, this.nome, this.telefone, this.email, this.perfil)
          .then((response => {
            this.resposta(1, response)
            this.preencher(this.cpf, this.nome, this.telefone, this.email, this.perfil);
          }))
          .catch((error => {
            this.resposta(2,error)
          }));
      } 
      else if(this.perfil == 4){
        this.userService.alterarDadosGarcom(this.matricula, this.cpf, this.nome, this.telefone, this.email, this.perfil)
          .then((response => {
            this.resposta(1, response)
            this.preencher(this.cpf, this.nome, this.telefone, this.email, this.perfil);
          }))
          .catch((error => {
            this.resposta(2,error)
          }));
      }
    }
  }

  private preencher(cpf:string, nome:string, telefone:string, email:string, perfil:number):void{
    this.nome = nome;
    this.telefone = telefone;
    this.email = email;
  }

  private resposta(tipo:number, msg:any):void{
    if(tipo == 1){
      this.mostrarMsg(msg.data,1);
      this.userService.recuperarCredencial();
    }else if(tipo == 2){
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
  }
}
