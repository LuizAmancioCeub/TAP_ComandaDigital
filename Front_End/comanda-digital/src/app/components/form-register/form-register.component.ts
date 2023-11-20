import { Component } from '@angular/core';
import { AxiosService } from 'src/app/services/axios.service';
import { RegisterService } from 'src/app/services/register.service';


@Component({
  selector: 'app-form-register',
  templateUrl: './form-register.component.html',
  styleUrls: ['./form-register.component.css']
})
export class FormRegisterComponent {

  constructor(private axiosService: AxiosService,private registerService:RegisterService){  }
  
  nome:string = "";
  cpf:string = "";
  telefone:string = "";
  senha:string = "";
  senhaB:string = "";
  
  
  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";

  
  onSubmitRegister(){
    // Verificar se as senhas são iguais
    const campos = this.registerService.conferirCampos(this.cpf, this.nome, this.telefone, this.senha, this.senhaB)
    if (campos !== "ok") {
     this.mostrarMsg(campos)
    } 
    else {
      this.mostrarErro = false; // Limpar mensagem de erro se já estiver sendo exibida
      this.register(this.cpf, this.nome, this.telefone, this.senha);
    }
  }

  register(cpf:string, nome:string,telefone:string, senha:string):void{
    this.axiosService.request(
      "POST",
      "/login/registrar",
      {
        cpf: cpf,
        nome: nome,
        telefone:telefone,
        senha:senha
      }
    ).then((response) => {
      // Lidar com a resposta bem-sucedida do servidor (usuário logado)
      this.mostrarMsg("cadastrado")
      // Redirecionar ou fazer outras ações necessárias após o login
    }).catch((error) => {
      if(error.response.status === 406) {
        this.mostrarMsg("406");
        console.clear
      }
      else{
        this.mostrarMsg("fail")
        console.clear
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
  
  
  mostrarMsg(tipo:string):void{
    this.mostrarErro = true;
    if(tipo == "campos"){
      this.alert = "warning"
      this.erro = "Todos os campos devem ser preenchidos"
      this.icon = "bi bi-exclamation-triangle-fill";
    } 
    else if(tipo == "cpf"){
      this.alert = "warning"
      this.erro = "Cpf inválido"
      this.icon = "bi bi-exclamation-triangle-fill";

    }
    else if(tipo == "nome"){
      this.alert = "warning"
      this.erro = "Nome inválido"
      this.icon = "bi bi-exclamation-triangle-fill";

    }
    else if(tipo == "telefone"){
      this.alert = "warning"
      this.erro = "Telefone inválido"
      this.icon = "bi bi-exclamation-triangle-fill";

    }
    else if(tipo == "senha"){
      this.alert = "warning"
      this.erro = "Senhas não conferem ou são inválidas"
      this.icon = "bi bi-exclamation-triangle-fill";
    }
    else if(tipo == "406"){
      this.alert = "warning"
      this.erro = "Já existe cadastro com o cpf ou telefone informado!!!"
      this.icon = "bi bi-exclamation-triangle-fill";

    }
    else if(tipo == "cadastrado"){
      this.alert = "success"
      this.erro = "Cadastro efetuado, faça o login para acessar nossos serviços"
      this.icon = "";
    }
     else{
      this.alert = "danger"
      this.erro = "Tente novamente mais tarde"
      this.icon = "bi bi-exclamation-triangle-fill";
    }

     setTimeout(() => {
      this.mostrarErro = false;
    }, 3000);
  }
}
