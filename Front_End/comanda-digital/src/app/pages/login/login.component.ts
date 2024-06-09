import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {AxiosService} from '../../services/axios.service'
import { FormLoginComponent } from 'src/app/components/form-login/form-login.component';
import { LoginService } from 'src/app/services/login.service';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private axiosService: AxiosService, private formService:LoginService,private route: ActivatedRoute ){}

  
mesa:string = '';
mesaCript:string = '';
  ngOnInit(): void {
     // Limpa o token ao acessar a rota principal
     this.axiosService.setAuthToken(null);
     this.route.queryParams.subscribe(params => {
      this.mesa = params['mesa'] != undefined ? params['mesa'].toString() : '';
    });
  }

  
}
