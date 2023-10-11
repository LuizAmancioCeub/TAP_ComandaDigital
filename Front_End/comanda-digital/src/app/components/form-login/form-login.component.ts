import { Component } from '@angular/core';


@Component({
  selector: 'app-form-login',
  templateUrl: './form-login.component.html',
  styleUrls: ['./form-login.component.css']
})
export class FormLoginComponent {
  

  // função para visualizar/ esconder senha
  public eye():void{
    const inputIcon:any= document.querySelector('.input__icon')
    const inputPassword:any = document.querySelector('.input__field')
   
    inputIcon.classList.toggle('ri-eye-off-line');
    inputIcon.classList.toggle('ri-eye-line');
    
    inputPassword.type = inputPassword.type === 'password' ? 'text' : 'password'
    
  }      
}
