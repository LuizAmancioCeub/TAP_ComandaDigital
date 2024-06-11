import { Component, Input, OnInit, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ComandaClienteData } from 'src/app/Models/ComandaData';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { UserService } from 'src/app/services/user.service';
  interface Pedido {
    idPedido:number,
    idItem: number;
    nomeItem: string;
    precoItem: number;
    quantidade: number;
    valor: number;
    horarioPedido: string;
    horarioEntrega:string;
    imagem:string;
    status: {
      id: number;
      status: string;
    };
  }
@Component({
  selector: 'app-card-comanda',
  templateUrl: './card-comanda.component.html',
  styleUrls: ['./card-comanda.component.css']
})
export class CardComandaComponent implements OnInit {

  load:boolean = true;
  constructor(private userService:UserService, private modalService:NgbModal){}
  ngOnInit(): void {
    this.initUserData();
    this.verificarStatus();
  }

  private async initUserData(): Promise<void> {
    this.userData = await this.userService.getUserData();
    if (this.userData !== null) {
      this.recuperarUser();
    } else {
      console.error("Erro ao recuperar dados do usuário");
    }
  }
  
  perfil:number=0;
  userData:CredencialsData|null = null;
  private recuperarUser(){
    if(this.userData != null){
      this.perfil = this.userData.perfil.id
  }
}

  confirmarPagamento:boolean = false;
  fecharComanda:boolean = false;
  verificarStatus(){
    if(this.status === "Aguardando Pagamento"){
      this.confirmarPagamento = true;
    }
    if(this.status === "Aberta"){
      this.fecharComanda = true;
    }
    this.load = false;
  }

@Input()idMesa:number =0;
@Input()valorTotal:number =0;
@Input()status:string ='';
@Input()idComanda:number =0;
@Input()nomeCliente:string = '';
@Input()cpfCliente:string= '';
@Input() pedidos: Pedido[] = [];
@Input() garcom:string = '';
@Input() dataAbertura:string = '';

openVerticallyCentered(content: TemplateRef<any>) {
  this.modalService.open(content, { centered: true,windowClass:'custom' });
}
close() {
  this.modalService.dismissAll();
}
}
