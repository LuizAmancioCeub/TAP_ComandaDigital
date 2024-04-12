import { Component, EventEmitter, Input, OnInit, Output, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ItensData } from 'src/app/Models/ItensData';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';
import { EventsService } from 'src/app/services/events.service';
import { LoaderService } from 'src/app/services/loader.service';

@Component({
  selector: 'app-contents',
  templateUrl: './contents.component.html',
  styleUrls: ['./contents.component.css']
})
export class ContentsComponent implements OnInit {
  constructor(private axiosService:AxiosService ,private categoriaService: CategoriaService, private eventService:EventsService,public loadService:LoaderService, private modalService:NgbModal) { }
  data:ItensData[] = [];
  dataDesativados:ItensData[] = [];
  msg:boolean = false
  txt:string = "";
  show:string=""
  categoria:number=0;

  load:boolean = true;
  @Input()perfil:number = 0;

  ngOnInit(): void {

    this.categoriaService.categoriaSelecionada$.subscribe((categoriaId) => {
      if (categoriaId !== null) {
        this.categoria = categoriaId;
        this.loadItems(categoriaId);
        if(this.perfil == 3){
          this.loadItemsDesativados(categoriaId);
        }
      }else{
        this.load = false;
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
        this.load = false;
        if(response.data == 0){
          this.itens = false;
        }
      }
    );
  }
  loadItemsDesativados(categoriaId: number): void {
    this.axiosService.request("GET", `/categoria/${categoriaId}/itensDesativados`, "").then(
      (response) => {
        this.dataDesativados = response.data;
        this.itens = true;
        this.load = false;
        if(response.dataDesativados == 0){
          this.itens = false;
        }
      }
    );
  }

  openVerticallyCentered(content: TemplateRef<any>) {
      this.modalService.open(content, { centered: true,windowClass:'custom' });
	}

  
}
