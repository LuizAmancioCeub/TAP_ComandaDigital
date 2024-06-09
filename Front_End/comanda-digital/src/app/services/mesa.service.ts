import { Injectable } from '@angular/core';
import { AxiosService } from './axios.service';
import { MesaData } from '../Models/mesaData';

@Injectable({
  providedIn: 'root'
})
export class MesaService {
  camera: boolean = false;
  alterarMesa: boolean = false;
  constructor(private axiosService:AxiosService) { }

   // Endpoints Item
   adicionarItem(nomeItem:string,descricao:string,preco:number,imagem:string,categoriaId:number,statusId:number):Promise<any>{
    return this.axiosService.request(
      "POST",
      `/item`,
      {
        nome:nomeItem,
        descricao:descricao,
        preco:preco,
        imagem:imagem,
        categoria:{
          id:categoriaId
        },
        status:{
          id:statusId
        }
      }
    )
  }

  alterarMesaCliente(cpf:string, novaMesa:number, mesaAtual:number):Promise<any>{
    return this.axiosService.request(
      "PUT",
      "/mesa/alterar",
      {
        cpf:cpf,
        novaMesa:novaMesa,
        mesaAtual:mesaAtual
      }
    )
  }

  recuperarMesas():Promise<any>{
    return this.axiosService.request(
      "GET",
      `/mesas`,
      ''
    )
  }

  recuperarById(id:number):Promise<any>{
    return this.axiosService.request(
      "GET",
      `/mesa/${id}`,
      ''
    )
  }

  mesa:number|null = null;
  salvarMesa(id:number){
    this.mesa = id;
  }

  recuperarMesa():number|null{
    return this.mesa;
  }
}
