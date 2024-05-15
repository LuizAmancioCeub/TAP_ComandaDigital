import { Component } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AxiosService } from 'src/app/services/axios.service';
import { RegisterService } from 'src/app/services/register.service';


@Component({
  selector: 'app-form-register',
  templateUrl: './form-register.component.html',
  styleUrls: ['./form-register.component.css']
})
export class FormRegisterComponent {

  constructor(private axiosService: AxiosService,private registerService:RegisterService,private modalService: NgbModal){  }
  
  nome:string = "";
  cpf:string = "";
  telefone:string = "";
  email:string = "";
  senha:string = "";
  senhaB:string = "";
  
  
  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";

  
  onSubmitRegister(){
    // Verificar se as senhas são iguais
    const campos = this.registerService.conferirCampos(this.cpf, this.nome, this.telefone, this.email, this.senha, this.senhaB)
    if (campos !== null) {
     this.mostrarMsg(campos,"")
    } 
    else {
      this.mostrarErro = false; // Limpar mensagem de erro se já estiver sendo exibida
      this.registerUser(this.cpf, this.nome, this.telefone, this.email, this.senha);
    }
  }

  formatarCPF() {
    // Remove todos os caracteres não numéricos do valor do CPF
    let cpfFormatado = this.cpf.replace(/\D/g, '');

    // Aplica a formatação do CPF (XXX.XXX.XXX-XX)
    cpfFormatado = cpfFormatado.replace(/(\d{3})(\d)/, '$1.$2');
    cpfFormatado = cpfFormatado.replace(/(\d{3})(\d)/, '$1.$2');
    cpfFormatado = cpfFormatado.replace(/(\d{3})(\d{1,2})$/, '$1-$2');

    // Atualiza o valor do campo com o CPF formatado
    this.cpf = cpfFormatado;
  }

  onKeyPress(event: KeyboardEvent) {
    // Obter o código da tecla pressionada
    const charCode = event.which || event.keyCode;

    // Permitir apenas números (códigos de tecla de 0 a 9)
    if (charCode < 48 || charCode > 57) {
      event.preventDefault(); // Impedir a entrada de caracteres não numéricos
    }
  }

  registerUser(cpf:string, nome:string,telefone:string,email:string, senha:string):void{
    this.registerService.register(cpf, nome, telefone,email, senha)
    .then((response) => {
      this.mostrarMsg("cadastrado","");
      this.cpf = ""
      this.nome = ""
      this.telefone = ""
      this.email = "";
      this.senha = ""
      this.senhaB = ""
      setTimeout(() => {
        this.close;
      }, 3000);
      // Redirecionar ou fazer outras ações necessárias após o login
    })
    .catch((error) => {
      const responseData = error.response.data;
      
      if (responseData && responseData.status === 400) {
        if(responseData.fields){
          const errorFields = responseData.fields;
          const fieldName = Object.keys(errorFields)[0];
          const fieldError = errorFields[fieldName];
          this.mostrarMsg("400",fieldError);
        }else{
          const errorDetail = responseData.detail;
          this.mostrarMsg("400",errorDetail);
        }
      } else {
        this.mostrarMsg("fail","");
      }
    });
  }
  // função para visualizar/ esconder senha
  public eye():void{
      const inputIcon:any= document.querySelector('.input__iconR')
      const inputPassword:any = document.querySelector('.input__fieldR')
     
      inputIcon.classList.toggle('ri-eye-off-line');
      inputIcon.classList.toggle('ri-eye-line');
      
      inputPassword.type = inputPassword.type === 'password' ? 'text' : 'password'
  }  
  
  
  mostrarMsg(tipo:string, mensagem:string):void{
    this.mostrarErro = true;
    this.alert = "warning";
    this.icon = "bi bi-exclamation-triangle-fill";
    switch (tipo) {
      case "campos":
          this.erro = "Todos os campos devem ser preenchidos";
          break;
      case "cpf":
          this.erro = "Cpf inválido";
          break;
      case "nome":
          this.erro = "Nome inválido";
          break;
      case "telefone":
          this.erro = "Telefone inválido";
          break;
      case "email":
          this.erro = "Email inválido";
          break;
      case "TamanhoSenha":
          this.erro = "Senha deve ter entre 6 à 12 caracteres";
          break;
      case "senha":
          this.erro = "Senhas não conferem ou são inválidas";
          break;
      case "400":
          this.erro = mensagem;
          break;
      case "cadastrado":
          this.alert = "success";
          this.erro = "Cadastro efetuado, faça o login para acessar nossos serviços";
          this.icon = "";
          break;
      default:
          this.alert = "danger";
          this.erro = "Tente novamente mais tarde";
          this.icon = "bi bi-exclamation-triangle-fill";
          break;
  }

     setTimeout(() => {
      this.mostrarErro = false;
    }, 3000);
  }

  close() {
    this.modalService.dismissAll();
  }
}
