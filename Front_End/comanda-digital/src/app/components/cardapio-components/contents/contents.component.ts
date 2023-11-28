import { Component, OnInit } from '@angular/core';
import { ItensData } from 'src/app/Models/ItensData';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-contents',
  templateUrl: './contents.component.html',
  styleUrls: ['./contents.component.css']
})
export class ContentsComponent implements OnInit {
  constructor(private axiosService:AxiosService ,private categoriaService: CategoriaService, private eventService:EventsService) { }
  isLoading:boolean = true;
  data:ItensData[] = [];
  msg:boolean = false
  txt:string = "";
  show:string=""

  ngOnInit(): void {
    this.categoriaService.categoriaSelecionada$.subscribe((categoriaId) => {
      if (categoriaId !== null) {
        this.loadItems(categoriaId);
      }
    });

    this.eventService.msg$.subscribe(({ msg, txt }) => {
      this.msg = msg;
      this.txt = txt;
      this.show = "show"
      setTimeout(() => {
        this.show = "";
      }, 2000);
      setTimeout(() => {
        this.msg = false;
      }, 3000);
    });
  }
  itens:boolean = true;
  loadItems(categoriaId: number): void {
    this.axiosService.request("GET", `/categoria/${categoriaId}/itens`, "").then(
      (response) => {
        this.data = response.data;
        this.itens = true;
        this.isLoading = false;
        if(response.data == 0){
          this.itens = false;
        }
      },
      () => {
        this.isLoading = false; // Marca como não mais carregando em caso de erro
      }
    );
  }
}
