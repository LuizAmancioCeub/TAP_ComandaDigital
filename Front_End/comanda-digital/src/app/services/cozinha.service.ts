import { Injectable } from '@angular/core';
import { AxiosService } from './axios.service';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CozinhaService {

  constructor(private axiosService:AxiosService) { }

  getPedidos(statusId:number):Promise<any>{
    return this.axiosService.request(
      "GET",
      `/pedidosCozinha/${statusId}`,
      ""
    );
  }

  entregarPedido(idPedido:number, statusId:number):Promise<any>{
    return this.axiosService.request(
      "PUT",
      `/pedido/${idPedido}/${statusId}`,
      ""
    )
  }

  private pedidoAttSource = new Subject<boolean>();
  pedidoAtt$ = this.pedidoAttSource.asObservable();

  emitPedido(pedidoAtt: boolean) {
    this.pedidoAttSource.next(pedidoAtt);
  }
  
}
