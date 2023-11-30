import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-contents-comanda',
  templateUrl: './contents.component.html',
  styleUrls: ['./contents.component.css']
})
export class ContentsComponentComanda implements OnInit, OnChanges {
  constructor(private eventService:EventsService){}
  ngOnChanges(changes: SimpleChanges): void {
   console.log(1);
  }

  msg:boolean = false
  txt:string = "";
  show:string=""
  
  ngOnInit(): void {
    this.eventService.msgPedido$.subscribe(({ msg, txt }) => {
      this.msg = msg;
      this.txt = txt;
      this.show = "show"
      setTimeout(() => {
        this.show = "";
      }, 2000);
      setTimeout(() => {
        this.msg = false;
      }, 3000);
    });
  }
}
