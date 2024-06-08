import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/internal/Subject';

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  private msgSource = new Subject<{ msg: boolean, txt: string }>();
  private msgPedidoSource = new Subject<{ msg: boolean, txt: string }>();
  private msgMesaSource = new Subject<{ msg: boolean, txt: string }>();

  private updateCategoriaSource = new Subject<{}>();
  private updateComandaSource = new Subject<{}>();

  private existsPedidoPreparoSource = new Subject<{pedidoPreparo:boolean}>();
  private existsPedidoEntregueSource = new Subject<{pedidoEntregue:boolean}>();

  msg$ = this.msgSource.asObservable();
  msgMesa$ = this.msgMesaSource.asObservable();
  msgPedido$ = this.msgPedidoSource.asObservable();
  categoria$ = this.updateCategoriaSource.asObservable();
  comanda$ = this.updateComandaSource.asObservable();

  existsPedidoPreparo$ = this.existsPedidoPreparoSource.asObservable();
  existsPedidoEntregue$ = this.existsPedidoEntregueSource.asObservable();

  emitMsg(msg:boolean, txt:string) {
    this.msgSource.next({ msg, txt });
  }

  emitMsgPedido(msg:boolean, txt:string) {
    this.msgPedidoSource.next({ msg, txt });
  }

  emitMsgMesa(msg:boolean, txt:string) {
    this.msgMesaSource.next({ msg, txt });
  }

  updateCategoria(){
    this.updateCategoriaSource.next({});
  }

  updateComanda(){
    this.updateComandaSource.next({});
  }

  existsPedidoPreparo(pedidoPreparo:boolean){
    this.existsPedidoPreparoSource.next({pedidoPreparo});
  }

  existsPedidoEntregue(pedidoEntregue:boolean){
    this.existsPedidoEntregueSource.next({pedidoEntregue});
  }

}
