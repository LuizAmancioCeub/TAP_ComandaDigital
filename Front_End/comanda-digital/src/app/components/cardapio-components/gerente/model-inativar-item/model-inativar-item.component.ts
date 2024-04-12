import { Component, Input, OnInit } from '@angular/core';
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
export class ModelInativarItemComponent implements OnInit {
  constructor(private gerenteservice:GerenteService,private modalService: NgbModal, private eventService:EventsService,  private categoriaService: CategoriaService){}
  
  @Input() itemId:number|null = null;
  @Input() nomeItem:string='';
  @Input() categoriaItem:number=0;
  @Input() statusItem:number=0;
  statusInativo:number = 2;
  statusAtivo:number = 1;
  acao:string=''

  ngOnInit(): void {
    if(this.statusItem == this.statusAtivo){
      this.acao = "Inativar"
    }else if( this.statusItem == this.statusInativo){
      this.acao = "Excluir"
    }
  }

  close() {
    this.modalService.dismissAll();
  }

  onSubmitInativar(){
    if(this.statusItem == this.statusAtivo){
      this.gerenteservice.updateStatusItem(this.itemId, this.statusInativo)
      .then((response) => {
        this.categoriaService.setCategoriaSelecionada(this.categoriaItem);
        this.close();
          this.eventService.emitMsg(true, "Item inativado com Sucesso");
      }).catch((error) => {
        this.close();
        const errorDetail = error.response.data.detail;
        this.eventService.emitMsg(true, errorDetail);
        console.clear;
      });
    } else if(this.statusItem == this.statusInativo){
      this.gerenteservice.excluirItem(this.itemId)
      .then((response) => {
        this.categoriaService.setCategoriaSelecionada(this.categoriaItem);
        this.close();
          this.eventService.emitMsg(true, "Item excluido com Sucesso");
      }).catch((error) => {
        this.close();
        const errorDetail = error.response.data.detail;
        this.eventService.emitMsg(true, errorDetail);
        console.clear;
      });
    }
    
  }
}
