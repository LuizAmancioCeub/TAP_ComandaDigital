import { Injectable } from '@angular/core';
import axios, { AxiosRequestConfig } from 'axios';

@Injectable({
  providedIn: 'root'
})
export class AxiosService {

  constructor() { 
    axios.defaults.baseURL = 'http://localhost:8080'; // definindo url base para requisição
    axios.defaults.headers.post['Content-Type'] = 'application/json'; // definindo o tipo json pra fzr rquisição
    
  }

  getAuthToken():string | null {
    return window.localStorage.getItem("auth_token");
  }

  setAuthToken(token:string | null):void{
    if(token !== null){
      window.localStorage.setItem("auth_token", token);
    } else{
      window.localStorage.removeItem("auth_token");
    }
  }

  
  async request(method: string, url: string, data: any): Promise<any> {
    try {
      const config: AxiosRequestConfig = {
        method: method,
        url: url,
        data: data
      };

      const token = this.getAuthToken();

      if (token) {
        config.headers = {
          Authorization: `Bearer ${token}`
        };
      }

      const response = await axios(config);

      // Verifica se a resposta contém um token JWT
      if (response.data.token) {
        this.setAuthToken(response.data.token);
      }

      return response;
    } catch (error) {
      throw error;
    }
  }

  
}
