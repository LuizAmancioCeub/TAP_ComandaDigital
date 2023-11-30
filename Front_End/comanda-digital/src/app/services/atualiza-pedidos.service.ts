import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AtualizaPedidosService {
  private atualizacaoPedidoSubject = new Subject<boolean>();

  atualizacaoPedido$ = this.atualizacaoPedidoSubject.asObservable();

  emitirAtualizacaoPedido(atualizado: boolean) {
    this.atualizacaoPedidoSubject.next(atualizado);
  }
}
