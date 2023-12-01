import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-msg',
  templateUrl: './msg.component.html',
  styleUrls: ['./msg.component.css']
})
export class MsgComponent {

  @Input() txt:string=""
  @Input() show:string=""
}
