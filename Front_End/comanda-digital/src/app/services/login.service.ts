import { EventEmitter, Injectable } from '@angular/core';
import { FormLoginComponent } from '../components/form-login/form-login.component';
import { AxiosService } from './axios.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private axiosService:AxiosService) { }

  conferirCampos(login: string, senha: string, mesa: any): String{
    if(login == "" || senha == "" || mesa == null || login == null || senha == null){
      return "campos";
    }  
    else{
      return "ok"
    }
  }

  conferirLogin(login:string, senha:string, mesa:number):string{
    return "fail"
  }



  
}
