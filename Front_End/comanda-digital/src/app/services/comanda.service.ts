import { Injectable } from '@angular/core';
import { AxiosService } from './axios.service';

@Injectable({
  providedIn: 'root'
})
export class ComandaService {

  constructor(private axiosService:AxiosService) { }

  getPedidosEmPreparo():Promise<any>{
    return this.axiosService.request(
      "GET",
      "/meusPedidosEmPreparo",
      ""
    );
  }    

  getPedidosEntregues():Promise<any>{
    return this.axiosService.request(
      "GET",
      "/meusPedidosEntregues",
      ""
    );
  }
  
  getComanda():Promise<any>{
    return this.axiosService.request(
      "GET",
      "/minhaComanda",
      ""
    );
  }  

  updatePedido(idPedido:number,quantidade:number, valor:number, observacao:string):Promise<any>{
    return this.axiosService.request(
      "PUT",
      `/pedido/${idPedido}`,
      {
        quantidade: quantidade,
        valor: valor,
        observacao: observacao
      }
    );
  }

  cancelPedido(idPedido:number, idStatus:number):Promise<any>{
    return this.axiosService.request(
      "PUT",
      `/pedido/${idPedido}/${idStatus}`,
      ""
    );
  }

  getComandasByMesa(idMesa:number):Promise<any>{
    return this.axiosService.request(
      "GET",
      `/consultarComanda/mesa/${idMesa}`,
      ""
    );
  }

  getComandaByNumero(id:number):Promise<any>{
    return this.axiosService.request(
      "GET",
      `/consultarComanda/numero/${id}`,
      ""
    );
  }
  getComandaByCPF(cpf:string):Promise<any>{
    return this.axiosService.request(
      "GET",
      `/consultarComanda/${cpf}`,
      ""
    );
  }
  
}
