import { Component, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';
import { EventsService } from 'src/app/services/events.service';
import { GerenteService } from 'src/app/services/gerente.service';

@Component({
  selector: 'app-model-inativar-item',
  templateUrl: './model-inativar-item.component.html',
  styleUrls: ['./model-inativar-item.component.css']
})
export class ModelInativarItemComponent {
  constructor(private gerenteservice:GerenteService,private modalService: NgbModal, private eventService:EventsService,  private categoriaService: CategoriaService){}
  
  @Input() itemId:number|null = null;
  @Input() nomeItem:string='';
  statusInativo:number = 2;

  close() {
    this.modalService.dismissAll();
  }

  onSubmitInativar(){
    this.gerenteservice.updateStatusItem(this.itemId, this.statusInativo)
      .then((response) => {
        this.close();
        setTimeout(() => {
          this.eventService.emitMsg(true, "Item inativado com Sucesso");
        }, 300);
      }).catch((error) => {
        this.close();
        const errorDetail = error.response.data.detail;
        this.eventService.emitMsg(true, errorDetail);
        console.clear;
      });
  }
}
