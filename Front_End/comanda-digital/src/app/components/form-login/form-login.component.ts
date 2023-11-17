import { Component, EventEmitter, Output, inject, TemplateRef, ViewEncapsulation } from '@angular/core';
import { LoginComponent } from 'src/app/pages/login/login.component';
import { AxiosService } from 'src/app/services/axios.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';


@Component({
  selector: 'app-form-login',
  templateUrl: './form-login.component.html',
  styleUrls: ['./form-login.component.css']
})
export class FormLoginComponent {
  private modalService = inject(NgbModal);
  
  constructor(private axiosService: AxiosService , private router: Router){}

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
    if(this.login == "" || this.senha == "" || this.mesa == null || this.login == null || this.senha == null){
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "Todos os campos devem ser preenchidos"
      this.icon = "bi bi-exclamation-triangle-fill";

       // Definir um atraso de 3 segundos para limpar a mensagem de erro
      setTimeout(() => {
        this.mostrarErro = false;
      }, 2000);
    } 
    else{
      //this.onSubmitLoginEvent.emit({"login": this.login, "senha": this.senha, "mesa": this.mesa});
     // console.log(this.login, this.senha, this.mesa)
     this.link="cardapio";
     this.router.navigate([this.link]);

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
  
  openVerticallyCentered(content: TemplateRef<any>) {
		this.modalService.open(content, { centered: true });
	}

  
}
