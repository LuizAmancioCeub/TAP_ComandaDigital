import { Component } from '@angular/core';

@Component({
  selector: 'app-form-register',
  templateUrl: './form-register.component.html',
  styleUrls: ['./form-register.component.css']
})
export class FormRegisterComponent {
  
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
    if (this.senha !== this.senhaB) {
      // Senhas não conferem, mostrar mensagem de erro
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "As senhas não são iguais"
      this.icon = "bi bi-exclamation-triangle-fill";
       // Definir um atraso de 3 segundos para limpar a mensagem de erro
       setTimeout(() => {
        this.mostrarErro = false;
      }, 2000);
    } 
    else if(this.nome == ""|| this.cpf == "" || this.telefone == "" || this.senha == ""){
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "Todos os campos precisam ser preenchidos"
      this.icon = "bi bi-exclamation-triangle-fill";
       // Definir um atraso de 3 segundos para limpar a mensagem de erro
       setTimeout(() => {
        this.mostrarErro = false;
      }, 2000);
    }
    else {
      // Senhas conferem, realizar ação desejada
      this.mostrarErro = false; // Limpar mensagem de erro se já estiver sendo exibida
      // Continuar com a lógica para o envio do formulário, se necessário
    }
  }

  // função para visualizar/ esconder senha
  public eye():void{
      const inputIcon:any= document.querySelector('.input__icon')
      const inputPassword:any = document.querySelector('.input__field')
     
      inputIcon.classList.toggle('ri-eye-off-line');
      inputIcon.classList.toggle('ri-eye-line');
      
      inputPassword.type = inputPassword.type === 'password' ? 'text' : 'password'
  }   
}
