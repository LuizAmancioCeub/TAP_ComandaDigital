import { Injectable } from '@angular/core';
import { AxiosService } from './axios.service';

@Injectable({
  providedIn: 'root'
})
export class MesaService {

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
}
