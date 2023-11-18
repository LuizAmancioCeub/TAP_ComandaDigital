import { Component, EventEmitter, TemplateRef, inject } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MesaData } from 'src/app/Models/mesaData';
import { AxiosService } from 'src/app/services/axios.service';

@Component({
  selector: 'app-teste-content',
  templateUrl: './teste-content.component.html',
  styleUrls: ['./teste-content.component.css']
})
export class TesteContentComponent {
  private modalService = inject(NgbModal);
  modalRef:any;
  
  data:MesaData[] = [];
  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";
  link:string = "";


  id:number|null=null;
  mesaSelect:any;

  onSubmit(){
    console.log(this.id)
    this.onRegister({"id": this.id});
  }

  onRegister(tst: any): void {
    if(!this.id||this.id == null){
      this.mostrarErro = true;
      this.alert = "warning"
      this.erro = "Todos os campos devem ser preenchidos"
      this.icon = "bi bi-exclamation-triangle-fill";

       // Definir um atraso de 3 segundos para limpar a mensagem de erro
      setTimeout(() => {
        this.mostrarErro = false;
      }, 2000);
    } else{
      this.axiosService.request(
        "POST",
        "/mesa",
        {
          id: tst.id
        }
      ).then(
        (response) => {
          // Lógica de sucesso (se necessário)
          this.getMesas(); // Atualiza a lista de mesas após adicionar uma nova mesa
          this.mostrarErro = true;
          this.alert = "success"
          this.erro = "Mesa cadastrada com Sucesso"
          this.icon = "bi bi-check-circle";
          // Definir um atraso de 3 segundos para limpar a mensagem de erro
            setTimeout(() => {
            this.mostrarErro = false;
          }, 2000);
        },
        (error) => {
          this.mostrarErro = true;
          this.alert = "warning"
          this.erro = "Mesa já existe"
          this.icon = "bi bi-exclamation-triangle-fill";
          // Definir um atraso de 3 segundos para limpar a mensagem de erro
            setTimeout(() => {
            this.mostrarErro = false;
          }, 2000);
    
        }
      );
    }
    
  }

  constructor(private axiosService: AxiosService){}

  ngOnInit():void{
    this.getMesas();
  }

  getMesas():void{
    this.axiosService.request("GET", "/mesas", "").then(
      (response) => {
      console.log(response);
      this.data = response.data;
      }
    );
  }

  excluirMesa:boolean = true;
  
  deleletMesas(id:number):void{
    console.log(id)
    this.axiosService.request("DELETE","/mesa/"+id, "")
    .then(
      (response) =>{
         // Lógica de sucesso (se necessário)
         this.getMesas(); // Atualiza a lista de mesas após adicionar uma nova mesa
         this.excluirMesa = false;
         this.alert = "success"
         this.erro = "Mesa Excluida com Sucesso"
         this.icon = "bi bi-check-circle";
         // Definir um atraso de 3 segundos para limpar a mensagem de erro
           setTimeout(() => {
            this.modalRef.close('Mesa excluída com sucesso');
         }, 2000);
            setTimeout(() => {
            this.excluirMesa = true;
          }, 2500);
       },
       (error) => {
        this.excluirMesa = false;
         this.alert = "danger"
         this.erro = "Não foi possível excluir Mesa"
         this.icon = "bi bi-exclamation-triangle-fill";
         // Definir um atraso de 3 segundos para limpar a mensagem de erro
         setTimeout(() => {
          this.modalRef.close('Não foi possível excluir Mesa');
       }, 2000);
          setTimeout(() => {
          this.excluirMesa = true;
        }, 2500);
   
       }
    );   
  }

  openVerticallyCentered(content: any, mesa:any) {
    this.mesaSelect = mesa;
		this.modalRef = this.modalService.open(content, { centered: true });
   
	}
  
}
