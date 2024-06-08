import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {

  constructor() { }

  private tabelaComandaLoaded = new BehaviorSubject<boolean>(false);
  private tabelaPreparoLoaded = new BehaviorSubject<boolean>(false);

  tabelaComandaLoaded$ = this.tabelaComandaLoaded.asObservable();
  tabelaPreparoLoaded$ = this.tabelaPreparoLoaded.asObservable();

  setTabelaComandaLoaded(loaded: boolean) {
    this.tabelaComandaLoaded.next(loaded);
  }

  setTabelaPreparoLoaded(loaded: boolean) {
    this.tabelaPreparoLoaded.next(loaded);
  }
}
