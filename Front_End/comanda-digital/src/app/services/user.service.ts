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
  
  async verificarUsuario(): Promise<void> {
    try {
      const response = await this.axiosService.request("GET", "/myCredenciais", "");
      this.setUserData(response.data);
    } catch (error) {
      console.error("Erro ao verificar usuário: ", error);
    }
  }
}
