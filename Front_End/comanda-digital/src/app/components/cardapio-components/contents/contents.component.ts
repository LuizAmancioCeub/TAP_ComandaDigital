import { Component, OnInit } from '@angular/core';
import { ItensData } from 'src/app/Models/ItensData';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';

@Component({
  selector: 'app-contents',
  templateUrl: './contents.component.html',
  styleUrls: ['./contents.component.css']
})
export class ContentsComponent implements OnInit {
  constructor(private axiosService:AxiosService ,private categoriaService: CategoriaService) { }

  data:ItensData[] = [];

  ngOnInit(): void {
    this.categoriaService.categoriaSelecionada$.subscribe((categoriaId) => {
      if (categoriaId !== null) {
        this.loadItems(categoriaId);
      }
    });
  }
  itens:boolean = true;
  loadItems(categoriaId: number): void {
    this.axiosService.request("GET", `/categoria/${categoriaId}/itens`, "").then(
      (response) => {
        this.data = response.data;
        this.itens = true;
        if(response.data == 0){
          this.itens = false;
        }
      }
    );
  }
}
