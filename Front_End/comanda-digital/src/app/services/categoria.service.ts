import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  constructor() { }
  
  private categoriaSelecionadaSubject = new BehaviorSubject<number | null>(null);
  categoriaSelecionada$ = this.categoriaSelecionadaSubject.asObservable();

  setCategoriaSelecionada(categoriaId: number | null): void {
    this.categoriaSelecionadaSubject.next(categoriaId);
  }
}
