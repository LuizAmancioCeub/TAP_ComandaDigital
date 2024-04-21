import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FuncionariosService {

  constructor() { }

  private perfilSelecionadaSubject = new BehaviorSubject<number | null>(null);
  perfilSelecionada$ = this.perfilSelecionadaSubject.asObservable();

  setPerfilelecionada(idPerfil: number | null): void {
    this.perfilSelecionadaSubject.next(idPerfil);
  }
}
