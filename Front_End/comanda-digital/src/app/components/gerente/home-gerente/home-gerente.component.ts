import { Component, OnInit } from '@angular/core';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { AxiosService } from 'src/app/services/axios.service';
import { RelatoriosService } from 'src/app/services/relatorios.service';

@Component({
  selector: 'app-home-gerente',
  templateUrl: './home-gerente.component.html',
  styleUrls: ['./home-gerente.component.css']
})
export class HomeGerenteComponent {
  constructor(private axiosService:AxiosService, private relatorioService: RelatoriosService){}
  

  async getRelatorio(): Promise<void> {
    try {
      const response = await this.axiosService.request("GET", "/pedidos/gerarRelatorio", {
        headers: {
          'Accept': 'application/octet-stream'
        }
      });
      const a = document.createElement('a');
      a.href = "http://localhost:8080/pedidos/gerarRelatorio";
      a.download = 'relatorio.xls';
      a.click();
      window.URL.revokeObjectURL("http://localhost:8080/pedidos/gerarRelatorio");
    } catch (error) {
      console.error('Erro ao baixar o relatório:', error);
      throw error;
    }
  }
}
