import { Component, Input, OnChanges } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent {

  constructor(private modalService:NgbModal,private eventService:EventsService){}
  @Input()item:string="";
  @Input()itemId:string = "";
  @Input()imagem:string="";
  feedback:string = ""

  campo:string=""

  selectedStar: number = 0;

  stars: number[] = [1, 2, 3, 4,5];

  onStarClicked(star: number) {
    this.selectedStar = star;
    console.log(this.selectedStar)
  }

  submitRating() {
    if(this.selectedStar != 0){
      //console.log('Avaliação selecionada:', this.selectedStar+"\nFeedback: "+this.feedback);
      this.close();
      setTimeout(() => {
        this.eventService.emitMsgPedido(true, "Avaliação Registrada");
      }, 300);
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
