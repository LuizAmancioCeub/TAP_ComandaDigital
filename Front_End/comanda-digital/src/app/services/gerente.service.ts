import { Injectable } from '@angular/core';
import { AxiosService } from './axios.service';

@Injectable({
  providedIn: 'root'
})
export class GerenteService {

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
  
  updateItem(idItem:number|null, nomeItem:string,descricao:string,preco:number,imagem:string,categoriaId:number,statusId:number):Promise<any>{
    return this.axiosService.request(
      "PUT",
      `/item/${idItem}`,
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
  updateStatusItem(idCategoria:number|null, novoStatus:number):Promise<any>{
      return this.axiosService.request(
        "PUT",
        `/item/${idCategoria}/${novoStatus}`,
        ''
      )
    }
  
  excluirItem(idItem:number):Promise<any>{
    return this.axiosService.request(
      "DELETE",
      `/item/${idItem}`,
      ""
    );
  }
  
  
  // Endpoints Categorias
  adicionarCategoria(nomeCategoria:string):Promise<any>{
    return this.axiosService.request(
      "POST",
      `/categorias`,
      {
        categoria:nomeCategoria
      }
    )
  }
  
  updateCategoria(idCategoria:number, nomeCategoria:string):Promise<any>{
    return this.axiosService.request(
      "PUT",
      `/categorias/${idCategoria}`,
      {
        categoria: nomeCategoria
      }
      
    )
  }

  excluirCategoria(idCategoria:number):Promise<any>{
    return this.axiosService.request(
      "DELETE",
      `/categorias/${idCategoria}`,
      ""
    );
  }
}
