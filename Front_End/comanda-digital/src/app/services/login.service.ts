import { EventEmitter, Injectable } from '@angular/core';
import { FormLoginComponent } from '../components/form-login/form-login.component';
import { AxiosService } from './axios.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private isLoggedIn = false;

  setLoggedIn(value: boolean): void {
    this.isLoggedIn = value;
  }

  constructor(private axiosService:AxiosService) { }

  onLogin(login:string, senha:string, mesa:number):Promise<any>{
    return this.axiosService.request(
      "POST",
      "/login",
      {
        login: login,
        senha: senha,
        mesa: {
          id: mesa
        }
      }
    );
  }  

  

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

  // Método para verificar se o usuário está autenticado
  isAuthenticated(): boolean {
    return this.isLoggedIn;
  }

  // Método para simular o logout
  logout(): void {
    this.isLoggedIn = false;
  }

  getUserRole():Promise<string> {
    return new Promise((resolve, reject) => {
      this.axiosService.request("GET", "/myCredenciais", "").then(
        (response) => {
          const perfil = response.data.perfil.perfil;
          resolve(perfil);
        },
        (error) => {
          reject(error);
        }
      );
    });
  }

  
}
