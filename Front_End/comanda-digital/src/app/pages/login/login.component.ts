import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {AxiosService} from '../../services/axios.service'
import { FormLoginComponent } from 'src/app/components/form-login/form-login.component';
import { LoginService } from 'src/app/services/login.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private axiosService: AxiosService, private formService:LoginService ){}

  

  ngOnInit(): void {
     // Limpa o token ao acessar a rota principal
     this.axiosService.setAuthToken(null);
  }

  
}
