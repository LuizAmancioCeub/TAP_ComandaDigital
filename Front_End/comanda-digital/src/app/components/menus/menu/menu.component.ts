import { Component, Input, OnInit, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoriaData } from 'src/app/Models/CategoriaData';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {
  constructor(private axiosService:AxiosService, private eventService:EventsService, private categoriaService: CategoriaService,private modalService:NgbModal){}
 
  ngOnInit(): void {
    this.getCategorias();

    this.eventService.categoria$.subscribe(() => {
      this.getCategorias();
    });
  }
  data: CategoriaData[] = [];
  @Input()perfil:number=0;
 
  getCategorias(): void {
    this.axiosService.request("GET", "/categorias", "").then((response) => {
      this.data = response.data;
      this.sortCategories(); // Ofertas selecionado
    });
  }

  sortCategories(): void {
    const indexOfOfertas = this.data.findIndex(
      (categoria) => categoria.categoria === "Ofertas"
    );
    if (indexOfOfertas > -1) {
      const ofertas = this.data.splice(indexOfOfertas, 1)[0];
      this.data.unshift({ ...ofertas, active: true }); // Coloca 'Ofertas' no início do array com a propriedade 'active'
      this.loadItemsOfertas(); // chamando itens da categoria Ofertas
    }
  }
  loadItemsOfertas(): void {
    const ofertas = this.data.find(categoria => categoria.categoria === "Ofertas");
    if (ofertas) {
      this.categoriaSelecionada(ofertas.id);
    }
  }

  activateCategory(categoria: CategoriaData): void {
    this.data.forEach((cat) => {
      cat.active = false; // Desativa todas as categorias
    });
    categoria.active = true; // Ativa a categoria clicada
    this.categoriaSelecionada(categoria.id);
  }

  categoriaSelecionada(categoriaId: number): void {
    this.categoriaService.setCategoriaSelecionada(categoriaId);
  }

  openVerticallyCentered(content: TemplateRef<any>) {
    this.modalService.open(content, { centered: true , windowClass:'custom'});
}
  
}
