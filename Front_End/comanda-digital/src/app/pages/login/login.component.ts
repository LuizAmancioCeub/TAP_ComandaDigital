import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {AxiosService} from '../../services/axios.service'


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  
  constructor(private axiosService: AxiosService ){}

  onLogin(input:any):void{
    this.axiosService.request(
      "POST",
      "/login",
      {
        login: input.login,
        senha: input.senha,
        mesa: input.mesa
      }
    );
  }

  ngOnInit(): void {
    
  }

  
}
