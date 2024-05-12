import { Component, Input, TemplateRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ClienteData } from 'src/app/Models/ClienteData';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { AxiosService } from 'src/app/services/axios.service';

@Component({
  selector: 'app-menu-perfil',
  templateUrl: './menu-perfil.component.html',
  styleUrls: ['./menu-perfil.component.css']
})
export class MenuPerfilComponent {
  @Input()perfil:number = 0;
  @Input()nome:string='';
  @Input()mesa:string='';
  
 

  constructor(private axiosService:AxiosService, private modalService:NgbModal){ }
  ngOnInit(): void {
  // this.verificarUsuario();
  }

  openVerticallyCentered(content: TemplateRef<any>) {
		this.modalService.open(content, {size:'lg',centered: true,windowClass:'custom'});
	}

 
}
