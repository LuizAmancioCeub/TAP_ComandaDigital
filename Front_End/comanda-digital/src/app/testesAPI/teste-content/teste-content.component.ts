import { Component } from '@angular/core';
import { MesaData } from 'src/app/Models/mesaData';
import { AxiosService } from 'src/app/services/axios.service';

@Component({
  selector: 'app-teste-content',
  templateUrl: './teste-content.component.html',
  styleUrls: ['./teste-content.component.css']
})
export class TesteContentComponent {
  
  data:MesaData[] = [];

  constructor(private axiosService: AxiosService){}

  ngOnInit():void{
    
  /*  
    this.axiosService.request("GET", "/status", "").then(
      (response) => this.data = response.data.map((item: any) => item.status) //percorrer cada objeto na resposta e extrair a propriedade "status"
    );
*/
   this.axiosService.request("GET", "/mesas", "").then(
      (response) => {
      console.log(response);
      this.data = response.data;
      }
    );
  }
  
}
