import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoriaData } from 'src/app/Models/CategoriaData';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';
import { EventsService } from 'src/app/services/events.service';
import { GerenteService } from 'src/app/services/gerente.service';

@Component({
  selector: 'app-modal-categorias',
  templateUrl: './modal-categorias.component.html',
  styleUrls: ['./modal-categorias.component.css']
})
export class ModalCategoriasComponent implements OnInit {

  constructor(private gerenteService:GerenteService,private modalService: NgbModal, private eventService:EventsService,  private categoriaService: CategoriaService){}
  @Input() data: CategoriaData[] = [];
  editState: { [id: number]: boolean } = {};
  valoresIniciais: { [id: number]: string } = {}; // Armazena os valores iniciais das categorias
  botaoSalvarAtivo: { [id: number]: boolean } = {}; // Armazena o estado dos botões de salvar
  novoNomeCategoria: { [id: number]: string } = {};
  campoError: { [id: number]: string } = {}; 
  campoAddError:string=''
  novaCategoria:string='';

  
  ngOnInit(): void {
    this.dados();
  }

  editar(categoriaId:number):void{
    if(this.editState[categoriaId] == true){
      this.editState[categoriaId] = false;
    }else{
      this.editState[categoriaId] = true;
    }
  }

  isEdit(categoriaId: number): boolean {
    return this.editState[categoriaId] ?? false;
  }

  excluir(categoriaId:number):void{
    this.gerenteService.excluirCategoria(categoriaId)
      .then(
        (response) => {
          this.eventService.updateCategoria();
          const index = this.data.findIndex(categoria => categoria.id === categoriaId);
          if (index !== -1) {
              this.data.splice(index, 1);
          }
        })
      .catch((error) => {
        const errorDetail = error.response.data.detail;
        this.mostrarMsg(errorDetail)
    });;
  }
  dados():void{
    this.data.forEach( categoria => {
      this.valoresIniciais[categoria.id] = categoria.categoria;
    }
    )
  }

  salvar(categoriaId:number):void{
    if(this.novoNomeCategoria[categoriaId] && this.novoNomeCategoria[categoriaId].length > 3 && this.novoNomeCategoria[categoriaId].trim() !== ''){
      this.gerenteService.updateCategoria(categoriaId, this.novoNomeCategoria[categoriaId])
      .then(
        (response) => {
          this.eventService.updateCategoria();
          this.editState[categoriaId] = false;
          this.botaoSalvarAtivo[categoriaId]= false;
          this.valoresIniciais[categoriaId] = this.novoNomeCategoria[categoriaId]
        }
      )
      .catch((error) => {
        const responseData = error.response.data;
        if(responseData.fields){
          const errorFields = responseData.fields;
          const fieldName = Object.keys(errorFields)[0];
          const errorDetail = errorFields[fieldName];
          this.mostrarMsg(errorDetail)
        }  else{
          const errorDetail = responseData.detail;
          this.mostrarMsg(errorDetail)
        }
      })
    }else{
      this.campoError[categoriaId] = "campo-error"
      setTimeout(() => {
        this.campoError[categoriaId] = "";
      }, 1000);
    }
  }

  verificarEdicao(categoriaId: number, event: Event): void {
    // Verifica se o valor digitado é diferente do valor inicial
    const inputElement = event.target as HTMLInputElement;
    // Obtém o valor digitado
    const novoValor = inputElement.value;
    const valorInicial = this.valoresIniciais[categoriaId];
    if (novoValor !== valorInicial) {
        // Se for diferente, mostra o botão de salvar
        this.botaoSalvarAtivo[categoriaId] = true;
        this.novoNomeCategoria[categoriaId] = novoValor;
    } else {
        // Caso contrário, esconde o botão de salvar
        this.botaoSalvarAtivo[categoriaId] = false;
    }
}

adicionarCategoria(nomeCategoria:string):void{
   console.log(nomeCategoria)
  if(nomeCategoria  && nomeCategoria.length > 3 && nomeCategoria.trim() !== ''){
    this.gerenteService.adicionarCategoria(nomeCategoria)
    .then((response) => {
      this.eventService.updateCategoria();
    })
    .catch((error) => {
      const responseData = error.response.data;
        if(responseData.fields){
          const errorFields = responseData.fields;
          const fieldName = Object.keys(errorFields)[0];
          const errorDetail = errorFields[fieldName];
          this.mostrarMsg(errorDetail)
        }  else{
          const errorDetail = responseData.detail;
          this.mostrarMsg(errorDetail)
        }
    })
  }else{
    this.campoAddError = "campo-error"
      setTimeout(() => {
        this.campoAddError = "";
      }, 1000);
  }
}

mostrarBotaoSalvar(categoriaId: number): boolean {
    return this.botaoSalvarAtivo[categoriaId] ?? false;
}

  close() {
    this.modalService.dismissAll();
  }

  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";

  mostrarMsg(mensagem:string):void{
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = mensagem
      this.icon = "bi bi-exclamation-triangle-fill";
     // Definir um atraso de 3 segundos para limpar a mensagem de erro
     setTimeout(() => {
      this.mostrarErro = false;
    }, 3000);
  }

}
