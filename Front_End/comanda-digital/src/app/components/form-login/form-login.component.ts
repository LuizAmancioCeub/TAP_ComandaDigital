import { Component, EventEmitter, Output, inject, TemplateRef, ViewEncapsulation, OnInit, OnChanges, SimpleChanges, Input } from '@angular/core';
import { LoginComponent } from 'src/app/pages/login/login.component';
import { AxiosService } from 'src/app/services/axios.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { MesaData } from 'src/app/Models/mesaData';
import { MesaService } from 'src/app/services/mesa.service';


@Component({
  selector: 'app-form-login',
  templateUrl: './form-login.component.html',
  styleUrls: ['./form-login.component.css']
})
export class FormLoginComponent implements OnInit {
  private modalService = inject(NgbModal);
  data:CredencialsData[] = [];
  
  constructor(private axiosService: AxiosService , private loginService:LoginService, private router: Router, private mesaService:MesaService){}
  
  mesaSelect:boolean = false;
  ngOnInit(): void {
    this.getMesas();
  }

  @Output() onSubmitLoginEvent = new EventEmitter();

  login:string = "";
  senha:string = "";
 @Input()mesaQrCode:string = '';
 mesa:number = 0;

  
  mostrarErro: boolean = false;
  erro:string = "";
  erroMesa:string = "";
  alert:string = "";
  icon:string = "";
  link:string = "";

  onSubmitLogin():void{
   const campos = this.loginService.conferirCampos(this.login, this.senha, this.mesa)
   if(campos == "campos"){
    this.mostrarMsg("campos","")
   }
   else if(campos == "fail"){
    this.mostrarMsg("fail","")
   }
   else if(campos == "ok"){
     // Verifica se o campo de login contém '.' ou '-'
    if (this.login && (this.login.includes('.') || this.login.includes('-'))) {
      // Remove '.' e '-' do campo de login
      this.login = this.login.replace(/[.-]/g, '');
    }
    if(typeof this.mesa == "string"){
      this.mesa = parseFloat(this.mesa);
      this.onLogin( this.login, this.senha, this.mesa);
    }else{
      this.onLogin( this.login, this.senha, this.mesa);
    }
   
   }
  }

  onSubmitLoginVisitante():void{
      this.onLoginVisitante( "visitante12", "visitante", 0);
   }

  onLogin(login:string, senha:string, mesa:number|null):void{
    this.load = true;
    this.loginService.onLogin(login,senha,mesa)
      .then((response) => {
        const token = response.data.token;
        if (token) {
          this.axiosService.setAuthToken(token);
          this.verificarUsuario();
        } 
      })
      .catch((error) => {
        if (error.response.status === 400) {
          // Se o status for 400 (regra de negocio), trate aqui
          const errorDetail = error.response.data.detail;
          this.load = false;
          this.mostrarMsg("400",errorDetail);
        }else if(error.response.status === 403){
          this.load = false;
          this.mostrarMsg("403","");
        } else{
          this.load = false;
          this.mostrarMsg("","");
        }
      });
  }

  onLoginVisitante(login:string, senha:string, mesa:number):void{
    this.load = true;
    this.loginService.onLoginVisitante(login,senha,mesa)
      .then((response) => {
        const token = response.data.token;
        if (token) {
          this.axiosService.setAuthToken(token);
          this.verificarUsuario();
        } 
      })
      .catch((error) => {
        if (error.response.status === 400) {
          // Se o status for 400 (regra de negocio), trate aqui
          const errorDetail = error.response.data.detail;
          this.load = false;
          this.mostrarMsg("400",errorDetail);
        }else if(error.response.status === 403){
          this.load = false;
          this.mostrarMsg("403","");
        } else{
          this.load = false;
          this.mostrarMsg("","");
        }
      });
  }
  verificarUsuario():void{
    this.axiosService.request("GET", "/myCredenciais", "").then(
      (response) => {
      const perfil = response.data.perfil.id;
      if (perfil === 1 || perfil === 2) {

        this.loginService.setLoggedIn(true);
        this.mesaService.salvarMesa(this.mesa);
        this.load = false;
        this.router.navigate(['/cardapio']); // Rota para clientes

      }else if (perfil === 3) {

        this.loginService.setLoggedIn(true);
        this.router.navigate(['/gerente']); // Rota para usuários gerentes
        setTimeout(() => {
          this.load = false;
        }, 1000);
      }
       else if (perfil === 5) {

        this.loginService.setLoggedIn(true);
        this.load = false;
        this.router.navigate(['/cozinha']); // 

      } 
       else if (perfil === 4) {

        this.loginService.setLoggedIn(true);
        this.load = false;
        this.router.navigate(['/mesas']); // 
       }
       else if (perfil === 6) {

        this.loginService.setLoggedIn(true);
        this.load = false;
        this.router.navigate(['/caixa']); // 
       }
       else {
        // Rota padrão para outros perfis ou tratamento de erro
        this.load = false;
        this.router.navigate(['/']);
       }
      }
    ) .catch((error) => {
      if (error.response.status === 400) {
        // Se o status for 400 (regra de negocio), trate aqui
        const errorDetail = error.response.data.detail;
        this.load = false;
        this.mostrarMsg("400",errorDetail);
      }else if(error.response.status === 403){
        this.load = false;
        this.mostrarMsg("403","");
      } else{
        this.load = false;
        this.mostrarMsg("","");
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

  mostrarMsg(tipo:string, mensagem:string):void{
    if(tipo == "campos"){
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "Todos os campos devem ser preenchidos"
      this.icon = "bi bi-exclamation-triangle-fill";
    } 
    else if(tipo == "400"){
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = mensagem;
      this.icon = "bi bi-exclamation-triangle-fill";

    }
    else if(tipo == "403"){
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "Senha ou login incorretos"
      this.icon = "bi bi-exclamation-triangle-fill";

    }
    else if(tipo == "mesa"){
      this.alert = "warning"
      this.erroMesa = "Nenhuma mesa válida foi selecionada"
      this.icon = "bi bi-exclamation-triangle-fill";

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

  dataMesas:MesaData[] = [];
  getMesas():void{
    this.axiosService.request("GET", "/mesasDisponiveis", "").then(
      (response) => {
      this.dataMesas = response.data;
      this.verificarMesa();
      }
    );
  }
load:boolean = false;
  verificarMesa(){
    let mesaExists = false;
    const mesaRecuperada = this.mesaService.recuperarMesa();
    if(mesaRecuperada != null){
      this.mesa = mesaRecuperada;
    }
    if(this.mesa != 0 || this.mesaQrCode.trim() != ""){
      
      for (let i = 0; i < this.dataMesas.length; i++) {
        const mesa = this.dataMesas[i];
        let qr = mesa.qr_code.split('=');
        const mesaQ = this.mesaQrCode.split('=');
        
        if(mesa.id == this.mesa || qr[1] == mesaQ[0] || mesa.id == parseFloat(mesaQ[0])){
          this.mesaSelect = true;
          mesaExists = true;
          this.mesa = mesa.id;
          break;
        }
      }
    }
    if(!mesaExists || this.mesa == 0){
      this.mesa = 0;
      this.mostrarMsg("mesa", "")
    }
  }
  
}
