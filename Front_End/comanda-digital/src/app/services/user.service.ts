import { Injectable } from '@angular/core';
import { CredencialsData } from '../Models/CredencialsData';
import { AxiosService } from './axios.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userData:CredencialsData|null = null;
  constructor(private axiosService:AxiosService) {}

  setUserData(userData: CredencialsData | null): void {
    this.userData = userData;
  }

  async getUserData(): Promise<CredencialsData | null> {
    if (this.userData === null) {
      await this.verificarUsuario();
    }
    return this.userData;
  }

  async recuperarCredencial():Promise<CredencialsData | null>{
     await this.verificarUsuario();
     return this.userData;
  }
  
  async verificarUsuario(): Promise<void> {
    try {
      const response = await this.axiosService.request("GET", "/myCredenciais", "");
      this.setUserData(response.data);
    } catch (error) {
      console.error("Erro ao verificar usuário: ", error);
    }
  }

  alterarDadosCliente(cpf:string, nome:string, telefone:string, email:string, perfil:number):Promise<any>{
    return this.axiosService.request(
      "PUT",
      `/cliente`,
      {
        cpf:cpf,
        nome:nome,
        telefone:telefone,
        email:email,
        perfil:perfil
      }
    );
  }

  alterarDadosGerente(matricula:string, cpf:string, nome:string, telefone:string, email:string, perfil:number):Promise<any>{
    return this.axiosService.request(
      "PUT",
      `/gerente`,
      {
        matricula:matricula,
        cpf:cpf,
        nome:nome,
        telefone:telefone,
        email:email,
        perfil:perfil
      }
    );
  }

  alterarDadosGarcom(matricula:string,cpf:string, nome:string, telefone:string, email:string, perfil:number):Promise<any>{
    return this.axiosService.request(
      "PUT",
      `/garcom`,
      {
        matricula:matricula,
        cpf:cpf,
        nome:nome,
        telefone:telefone,
        email:email,
        perfil:perfil
      }
    );
  }
}
