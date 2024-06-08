import { Injectable } from '@angular/core';
import { AxiosService } from './axios.service';
import { ComandaClienteData, ComandaData } from '../Models/ComandaData';

@Injectable({
  providedIn: 'root'
})
export class ComandaService {
  comandaData: ComandaClienteData | null = null;

  constructor(private axiosService:AxiosService) {  }

  setComandaData(comandaData: ComandaClienteData | null): void {
    this.comandaData = comandaData;
  }

  async getComandaData(): Promise<ComandaClienteData | null> {
    if (this.comandaData === null) {
      await this.verificarComanda();
    }
    return this.comandaData;
  }
  
  async verificarComanda(): Promise<void> {
    try {
      const response = await this.axiosService.request("GET", "/minhaComanda", "");
      this.setComandaData(response.data);
    } catch (error) {
      console.error("Erro ao verificar comanda: ", error);
    }
  }

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
    ).then((response => {

    }));
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

  getMyComandas():Promise<any>{
    return this.axiosService.request(
      "GET",
      `/minhasComandas`,
      ""
    );
  }

  updateStatusComanda(status:number):Promise<any>{
    return this.axiosService.request(
      "PUT",
      `/minhaComanda/${status}`,
      ""
    );
  }
  
}
