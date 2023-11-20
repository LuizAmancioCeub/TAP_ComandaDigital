import { Component, EventEmitter, Output, inject, TemplateRef, ViewEncapsulation, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { LoginComponent } from 'src/app/pages/login/login.component';
import { AxiosService } from 'src/app/services/axios.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';


@Component({
  selector: 'app-form-login',
  templateUrl: './form-login.component.html',
  styleUrls: ['./form-login.component.css']
})
export class FormLoginComponent implements OnInit {
  private modalService = inject(NgbModal);
  
  constructor(private axiosService: AxiosService , private loginService:LoginService, private router: Router){}
  
  
  ngOnInit(): void {
  }

  @Output() onSubmitLoginEvent = new EventEmitter();

  login:string = "";
  senha:string = "";
  mesa:number|null = null;

  
  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";
  link:string = "";

  onSubmitLogin():void{
   const campos = this.loginService.conferirCampos(this.login, this.senha, this.mesa)
   if(campos == "campos"){
    this.mostrarMsg("campos")
   }
   else if(campos == "fail"){
    this.mostrarMsg("fail")
   }
   else if(campos == "ok"){
    if(typeof this.mesa == "string"){
      this.mesa = parseFloat(this.mesa);
      this.onLogin( this.login, this.senha, this.mesa);
    }else if(typeof this.mesa == "number"){
      this.onLogin( this.login, this.senha, this.mesa);
    }
   
   }
  }

  onLogin(login:string, senha:string, mesa:number):void{
    this.axiosService.request(
      "POST",
      "/login",
      {
        login: login,
        senha: senha,
        mesa: {
          id: mesa
        } 
      }
    ).then((response) => {
      const token = response.data.token;
      if (token) {
        this.axiosService.setAuthToken(token);
        this.router.navigate(['/cardapio']);
      } 
    })
    .catch((error) => {

      if (error.response.status === 404) {
        // Se o status for 404 (usuário não encontrado), trate aqui
        this.mostrarMsg("404");
        console.clear
      }
      
      else if(error.response.status === 406) {
        this.mostrarMsg("406");
        console.clear
      }
      
      else if(error.response.status === 403) {
        this.mostrarMsg("403");
        console.clear
      }
    });
  }
  // função para visualizar/ esconder senha
  public eye():void{
   
      const inputIcon:any= document.querySelector('.input__icon')
      const inputPassword:any = document.querySelector('.input__field')
     
      inputIcon.classList.toggle('ri-eye-off-line');
      inputIcon.classList.toggle('ri-eye-line');
      
      inputPassword.type = inputPassword.type === 'password' ? 'text' : 'password'
  }   
  
  openVerticallyCentered(content: TemplateRef<any>) {
		this.modalService.open(content, { centered: true });
	}

  mostrarMsg(tipo:string):void{
    if(tipo == "campos"){
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "Todos os campos devem ser preenchidos"
      this.icon = "bi bi-exclamation-triangle-fill";
    } 
    else if(tipo == "404"){
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "Usuário ainda não cadastrado"
      this.icon = "bi bi-exclamation-triangle-fill";

    }
    else if(tipo == "406"){
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "Mesa Indisponível"
      this.icon = "bi bi-exclamation-triangle-fill";

    }
    else if(tipo == "403"){
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "Senha ou login incorretos"
      this.icon = "bi bi-exclamation-triangle-fill";

    }
    else if(tipo == "logado"){
      this.mostrarErro = true;
      this.alert = "success"
      this.erro = "Bem-Vindo"
      this.icon = "";

    } 
     else{
      this.mostrarErro = true;
      this.alert = "danger"
      this.erro = "Tente novamente mais tarde"
      this.icon = "bi bi-exclamation-triangle-fill";
    }

     // Definir um atraso de 3 segundos para limpar a mensagem de erro
     setTimeout(() => {
      this.mostrarErro = false;
    }, 2000);
  }

  
}
