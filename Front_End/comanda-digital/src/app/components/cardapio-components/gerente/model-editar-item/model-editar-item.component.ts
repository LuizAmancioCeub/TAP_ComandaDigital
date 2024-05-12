import { Component, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoriaData } from 'src/app/Models/CategoriaData';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';
import { EventsService } from 'src/app/services/events.service';
import { GerenteService } from 'src/app/services/gerente.service';

@Component({
  selector: 'app-model-editar-item',
  templateUrl: './model-editar-item.component.html',
  styleUrls: ['./model-editar-item.component.css']
})
export class ModelEditarItemComponent {
  constructor(private axiosService:AxiosService ,private gerenteService:GerenteService,private modalService: NgbModal, private eventService:EventsService,  private categoriaService: CategoriaService){}

  ngOnInit(): void {
    this.getCategorias();
    this.statusAtt = this.statusIdItem === 1 ? 1 : this.statusIdItem === 2 ? 2 : 0;
  }
  data:CategoriaData[] = [];

  imagem: File | null = null; // Variável para armazenar o arquivo de imagem selecionado
  imagemPreview: string | ArrayBuffer | null = null; // Variável para armazenar a pré-visualização da imagem

   // Método para exibir a pré-visualização da imagem
   previewImage(event: any): void {
    const file = event.target.files[0]; // Obtém o arquivo de imagem selecionado

    // Verifica se o arquivo é uma imagem
    if (file && file.type.startsWith('image')) {
      const reader = new FileReader(); // Cria um objeto FileReader

      // Define o comportamento quando o FileReader terminar de ler o arquivo
      reader.onload = () => {
        this.imagem = file; // Armazena o arquivo de imagem selecionado
        this.imagemPreview = reader.result; // Define a pré-visualização da imagem
        this.imagemSet = file.name;
      };

      reader.readAsDataURL(file); // Lê o arquivo como um URL de dados
    }
  }

  @Input() nomeItem:string = ''
  @Input() itemId:number|null=null;
  @Input() descricao:string= ''
  @Input() preco:number|null|string = null;
  @Input() imagemSet:string = '';
  @Input() categoria:number = 0;
  @Input() statusIdItem:number=0;
  load:boolean = false;

  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";
  statusAtt:number=0;

  getCategorias(): void {
    if(this.load != true){
      this.axiosService.request("GET", "/categorias", "").then((response) => {
        this.data = response.data;
        this.sortCategories(); // Ofertas selecionado
        this.load = true;
      });
    }
  }

  sortCategories(): void {
    const indexOfCategoria = this.data.findIndex(
      (categoria) => categoria.id === this.categoria
    );
    if (indexOfCategoria > -1) {
      const cat = this.data.splice(indexOfCategoria, 1)[0];
      this.data.unshift({ ...cat, active: true }); // Coloca categoria atual do item no início do array com a propriedade 'active'
    }
  }
  
  close() {
    this.modalService.dismissAll();
  }

  onSubmitEditarItem(){
    this.preco = typeof this.preco === 'string' ? parseFloat(this.preco) : this.preco as number; // convertendo string para number
    this.categoria = typeof this.categoria === 'string' ? parseFloat(this.categoria) : this.categoria as number; // convertendo string para number
    if (this.nomeItem.trim() !== '' && this.descricao.trim() !== '' &&!isNaN(this.preco) && !isNaN(this.categoria)){
      this.gerenteService.updateItem(this.itemId,this.nomeItem, this.descricao, this.preco, this.imagemSet,this.categoria,this.statusAtt)
        .then((response) => {
          this.categoriaService.setCategoriaSelecionada(this.categoria);
          this.close();
          setTimeout(() => {
            this.eventService.emitMsg(true, "Item alterado com Sucesso");
          }, 300);
        }).catch((error) => {
          const responseData = error.response.data;
          if(responseData.fields){
            const errorFields = responseData.fields;
            const fieldName = Object.keys(errorFields)[0];
            const fieldError = errorFields[fieldName];
            this.mostrarMsg(fieldError);
          }else{
            const errorDetail = responseData.detail;
            this.mostrarMsg(errorDetail);
          }
      });
    }else{
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "Todos os campos devem ser preenchidos"
      this.icon = "bi bi-exclamation-triangle-fill";
       // Definir um atraso de 3 segundos para limpar a mensagem de erro
     setTimeout(() => {
      this.mostrarErro = false;
    }, 2000);
    }
  }

  
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

onKeyPress(event: KeyboardEvent) {
  // Obter o código da tecla pressionada
    const charCode = event.which || event.keyCode;

   // Verificar se o charCode corresponde a uma vírgula
   if (charCode === 44) {
    // Substituir a vírgula por um ponto
    const inputElement = event.target as HTMLInputElement;
    const currentValue = inputElement.value;
    const cursorPosition = inputElement.selectionStart ?? 0;
    const newValue = currentValue.substring(0, cursorPosition) + '.' + currentValue.substring(cursorPosition + 1);
    inputElement.value = newValue;

    // Atualizar o cursor para a posição após a substituição
    inputElement.setSelectionRange(cursorPosition + 1, cursorPosition + 1);
    
    // Impedir o comportamento padrão da tecla
    event.preventDefault();
  }
  // Permitir apenas números (códigos de tecla de 0 a 9)
  if ((charCode < 48 && charCode != 46) || charCode > 57) {
    event.preventDefault(); // Impedir a entrada de caracteres não numéricos
  }
}
}
