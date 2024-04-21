import { Component, Input, TemplateRef, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoriaData } from 'src/app/Models/CategoriaData';
import { PerfilData } from 'src/app/Models/perfilData';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';
import { EventsService } from 'src/app/services/events.service';
import { FuncionariosService } from 'src/app/services/funcionarios.service';

@Component({
  selector: 'app-menu-funcionarios',
  templateUrl: './menu-funcionarios.component.html',
  styleUrls: ['./menu-funcionarios.component.css']
})
export class MenuFuncionariosComponent implements OnInit {
  constructor(private axiosService:AxiosService, private eventService:EventsService, private funcionarioService: FuncionariosService,private modalService:NgbModal){}
 
  ngOnInit(): void {
    this.getPerfis();
  }
  data:PerfilData[] = [];
 
  getPerfis(): void {
    this.axiosService.request("GET", "/gerente/funcionariosPerfil", "").then((response) => {
      this.data = response.data;
      this.sortPerfil(); // Ofertas selecionado
    });
  }

  sortPerfil(): void {
    const indexOfOfertas = this.data.findIndex(
      (perfil) => perfil.perfil === "Gerente"
    );
    if (indexOfOfertas > -1) {
      const ofertas = this.data.splice(indexOfOfertas, 1)[0];
      this.data.unshift({ ...ofertas, active: true }); // Coloca 'Ofertas' no início do array com a propriedade 'active'
      this.loadItemsOfertas(); // chamando itens da categoria Ofertas
    }
  }
  loadItemsOfertas(): void {
    const ofertas = this.data.find(perfil => perfil.perfil === "Gerente");
    if (ofertas) {
      this.perfilelecionado(ofertas.id);
    }
  }

  activateCategory(perfil: PerfilData): void {
    this.data.forEach((cat) => {
      cat.active = false; // Desativa todas as categorias
    });
    perfil.active = true; // Ativa a categoria clicada
    this.perfilelecionado(perfil.id);
  }

  perfilelecionado(idPerfil: number): void {
    this.funcionarioService.setPerfilelecionada(idPerfil);
  }

  openVerticallyCentered(content: TemplateRef<any>) {
    this.modalService.open(content, { centered: true , windowClass:'custom'});
}
}
