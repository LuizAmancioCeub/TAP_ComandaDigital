import { Component, Input, OnInit, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoriaData } from 'src/app/Models/CategoriaData';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { FuncionariosData } from 'src/app/Models/FuncionariosData';
import { AxiosService } from 'src/app/services/axios.service';
import { CategoriaService } from 'src/app/services/categoria.service';
import { EventsService } from 'src/app/services/events.service';
import { FuncionariosService } from 'src/app/services/funcionarios.service';

@Component({
  selector: 'app-manter-funcionarios',
  templateUrl: './manter-funcionarios.component.html',
  styleUrls: ['./manter-funcionarios.component.css']
})
export class ManterFuncionariosComponent implements OnInit {
  constructor(private axiosService:AxiosService, private eventService:EventsService, private funcionarioService: FuncionariosService,private modalService:NgbModal){}
 
  load:boolean = true;
  idPerfil:number=0;
  ngOnInit(): void {
    this.verificarUsuario();
    
    this.funcionarioService.perfilSelecionada$.subscribe((idPerfil) => {
      if (idPerfil !== null) {
        this.idPerfil = idPerfil;
        this.loadItems(idPerfil);
      }else{
        this.load = false;
      }
    });
  }
  dataUser:CredencialsData[] = [];
  perfil:number = 0;
  nomeUser:string='';
 
   verificarUsuario():void{
     this.axiosService.request("GET", "/myCredenciais", "").then(
       (response) => {
         this.perfil = response.data.perfil.id
         this.nomeUser = response.data.nome;
     });
   }
   cachedResponses: { [perfilId: number]: any } = {};
   dataFunc:FuncionariosData[] =[]
   itens:boolean=true;
   loadItems(perfilId: number): void {
    if(this.cachedResponses[perfilId]){
      this.verificarCampos(perfilId);
      this.dataFunc = this.cachedResponses[perfilId];
      this.itens = true;
      this.load = false;
    }else{
      this.axiosService.request("GET", `/gerente/funcionarios/${perfilId}`, "").then(
        (response) => {
          this.verificarCampos(perfilId);
          this.dataFunc = response.data;
          this.itens = true;
          this.load = false;
          this.cachedResponses[perfilId] = response.data;
          if(response.data == 0){
            this.itens = false;
          }
        } ,
        (error) => {
          console.log(error)
        }
      );
    }
  }
  openVerticallyCentered(content: TemplateRef<any>) {
    this.modalService.open(content, { centered: true , windowClass:'custom'});
}

pessoa:boolean=false;
cpf:string='';
matricula:string=''
telefone:string=''
  verificarCampos(perfilId:number){
    if (perfilId == 3 || perfilId == 4) {
      this.pessoa = true;
    } else{
      this.pessoa = false;
    }
  }
}
