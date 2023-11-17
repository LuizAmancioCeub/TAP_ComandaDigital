import { Injectable } from '@angular/core';
import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
export class AxiosService {

  constructor() { 
    axios.defaults.baseURL = 'http://localhost:8080'; // definindo url base para requisição
    axios.defaults.headers.post['Content-Type'] = 'application/json'; // definindo o tipo json pra fzr rquisição
    
  }

  
  request(method: string, url: string, data: any): Promise<any>{
    return axios({
      method:method,
      url:url,
      data:data
    });
  }

  
}
