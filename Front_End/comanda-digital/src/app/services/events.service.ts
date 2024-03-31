import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/internal/Subject';

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  private msgSource = new Subject<{ msg: boolean, txt: string }>();
  private msgPedidoSource = new Subject<{ msg: boolean, txt: string }>();

  private updateCategoriaSource = new Subject<{}>();


  msg$ = this.msgSource.asObservable();
  msgPedido$ = this.msgPedidoSource.asObservable();
  categoria$ = this.updateCategoriaSource.asObservable();

  emitMsg(msg:boolean, txt:string) {
    this.msgSource.next({ msg, txt });
  }

  emitMsgPedido(msg:boolean, txt:string) {
    this.msgPedidoSource.next({ msg, txt });
  }

  updateCategoria(){
    this.updateCategoriaSource.next({});
  }


}
