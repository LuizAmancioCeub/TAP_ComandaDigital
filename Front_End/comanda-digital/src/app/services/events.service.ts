import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/internal/Subject';

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  private msgSource = new Subject<{ msg: boolean, txt: string }>();

  msg$ = this.msgSource.asObservable();

  emitMsg(msg:boolean, txt:string) {
    this.msgSource.next({ msg, txt });
  }
}
