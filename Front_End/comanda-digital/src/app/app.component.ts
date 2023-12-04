import { AfterViewInit, Component, OnInit } from '@angular/core';
import { LoaderService } from './services/loader.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {
  title = 'Comanda-Digital';
  constructor(public  loaderService:LoaderService){}


  ngOnInit() {

  }

  ngAfterViewInit() {
   
  }
}
