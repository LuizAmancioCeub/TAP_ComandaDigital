import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { AxiosService } from 'src/app/services/axios.service';
import { EventsService } from 'src/app/services/events.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit {

  constructor(private modalService:NgbModal,private eventService:EventsService, private axiosService:AxiosService, private userService:UserService){}
  private userData:CredencialsData|null = null;
  ngOnInit(): void {
    this.recuperarAvaliacao()
  }

  private recuperarUser(){
    if(this.userData != null){
      this.cpf = this.userData.cpf
    }
  }
  @Input()item:string="";
  @Input()itemId:number = 0;
  @Input()imagem:string="";
  feedback:string = ""

  campo:string=""

  selectedStar: number = 0;

  stars: number[] = [1, 2, 3, 4,5];

  onStarClicked(star: number) {
    this.selectedStar = star;
  }

  @Input()cpf:string = "";
  recuperarAvaliacao(){
    this.axiosService.request(
      "GET",
      `/feedback/${this.cpf}/${this.itemId}`,
      ""
    ).then((response =>{
      if(response.data.avaliacao != null){
        this.feedback = response.data.comentario != null ? response.data.comentario : '' ;
        this.selectedStar = response.data.avaliacao;
        this.request = "PUT"
      }
    })).catch((error => {
      console.log(error)
    }))
  }

  mostrarErro:boolean = false;
  error:string = '';
  request:string = "POST"
  submitRating() {
    if(this.selectedStar > 0){
      this.axiosService.request(
        this.request,
        `/feedback`,
        {
          avaliacao:this.selectedStar,
          comentario:this.feedback,
          nuItem:this.itemId
        }
      ).then((response => {
        this.close();
        setTimeout(() => {
          this.eventService.emitMsgPedido(true, "Avaliação Registrada");
        }, 500);
      })).catch((error => {
        const responseData = error.response.data;
        if(responseData.fields){
          const errorFields = responseData.fields;
          const fieldName = Object.keys(errorFields)[0];
          const fieldError = errorFields[fieldName];
          this.error = fieldError
        }  else{
          const errorDetail = responseData.detail;
          this.error = errorDetail
        }
        this.mostrarErro = true;
        setTimeout(() => {
          this.mostrarErro = false;
        }, 3000);
      }))
     
    }else{
      this.campo = "vai-tremer"
      setTimeout(() => {
        this.campo = "";
      }, 1000);
    }
    
  }

  close() {
    this.modalService.dismissAll();
  }
}
