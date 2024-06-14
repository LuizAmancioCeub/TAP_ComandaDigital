package com.comandadigital.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comandadigital.models.ItemRelatorio;
import com.comandadigital.models.PedidoModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.repositories.PedidoRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RelatorioService {

	@Autowired
    private PedidoRepository pedidoRepository;
	
	public void gerarRelatorioSemanal(List<PedidoModel> pedidos) throws IOException {
			log.info("Gerando arquivo");
		 LocalDateTime now = LocalDateTime.now();
	        LocalDateTime umaSemanaAtras = now.minus(1, ChronoUnit.WEEKS);

	        //List<PedidoModel> pedidos = pedidoRepository.findPedidosEntreDatas(umaSemanaAtras, now);

	        Map<String, ItemRelatorio> itemRelatorioMap = new HashMap<>();

	        for (PedidoModel pedido : pedidos) {
	            String itemName = pedido.getItem().getNome();
	            ItemRelatorio itemRelatorio = itemRelatorioMap.getOrDefault(itemName, new ItemRelatorio(itemName));
	            itemRelatorio.incrementarQuantidade(pedido.getQuantidade());
	            itemRelatorio.adicionarTempoEntrega(pedido.getHorarioPedido(), pedido.getHorarioEntrega());
	            itemRelatorio.adicionarValorTotal(pedido.getValor() * pedido.getQuantidade());
	            itemRelatorioMap.put(itemName, itemRelatorio);
	        }

	        try (var workBook = new XSSFWorkbook(); var outputSream = new FileOutputStream("relatorio_semanal")) {
	        	
	        	var planilha = workBook.createSheet("Lista Pedidos");

	            // Cabeçalhos
	            Row headerRow = planilha.createRow(0);
	            headerRow.createCell(0).setCellValue("Item");
	            headerRow.createCell(1).setCellValue("Quantidade Pedida");
	            headerRow.createCell(2).setCellValue("Média Tempo Entrega (min)");
	            headerRow.createCell(3).setCellValue("Valor Total");

	            // Dados
	            int numeroLinha = 1;
	            for (ItemRelatorio itemRelatorio : itemRelatorioMap.values()) {
	            	var linha = planilha.createRow(numeroLinha++);
	            	linha.createCell(0).setCellValue(itemRelatorio.getNome());
	            	linha.createCell(1).setCellValue(itemRelatorio.getQuantidade());
	            	linha.createCell(2).setCellValue(itemRelatorio.getMediaTempoEntrega());
	            	linha.createCell(3).setCellValue(itemRelatorio.getValorTotal());
	            }

	            workBook.write(outputSream);
	            //return new ByteArrayInputStream(out.toByteArray());
	        }catch (FileNotFoundException e) {
				log.error("Arquivo não encontrado");
			}catch (IOException e) {
				log.error("Erro ao processar arquivo");
			}
	        log.info("Arquivo Gerado com Sucesso");
	}
	
	// metódo para requisição
	 public void gerarRelatorioSemanal2(HttpServletResponse response) throws IOException {
	        LocalDateTime now = LocalDateTime.now();
	        LocalDateTime startDate = now.minusDays(7);
	        try {
	        	
	        	 //Aba de Pedidos finalizados
	        	
	        	List<PedidoModel> pedidos = pedidoRepository.findPedidosEntreDatas(Arrays.asList(StatusModel.CANCELADO),startDate, now);
	  	        
	  	        Map<String, ItemRelatorio> itemRelatorioMap = new HashMap<>();

	  	        for (PedidoModel pedido : pedidos) {
	  	            String itemName = pedido.getItem().getNome();
	  	            ItemRelatorio itemRelatorio = itemRelatorioMap.getOrDefault(itemName, new ItemRelatorio(itemName));
	  	            itemRelatorio.setIdItem(pedido.getItem().getId());
	  	            itemRelatorio.incrementarQuantidade(pedido.getQuantidade());
	  	            itemRelatorio.adicionarTempoEntrega(pedido.getHorarioPedido(), pedido.getHorarioEntrega());
	  	            itemRelatorio.adicionarValorTotal(pedido.getValor());
	  	            itemRelatorioMap.put(itemName, itemRelatorio);
	  	        }
	  	        
	  	        HSSFWorkbook workbook = new HSSFWorkbook();
	  	        
	  	        HSSFSheet sheet = workbook.createSheet("Pedidos Sucesso");
	  	        HSSFRow row = sheet.createRow(0);
	  	        
	  	        // Adicionar cabeçalhos
	  	        	row.createCell(0).setCellValue("ID");
	  	            row.createCell(1).setCellValue("Item");
	  	            row.createCell(2).setCellValue("Quantidade de Pedidos");
	  	            row.createCell(3).setCellValue("Tempo Médio de Entrega (minutos)");
	  	            row.createCell(4).setCellValue("Valor Total Arrecadado (R$)");

	  	            // Adicionar dados
	  	            int rowNum = 1;
	  	            double valorTotalItens = 0;
	  	            int qntdTotalItens = 0;
	  	            for (ItemRelatorio itemRelatorio : itemRelatorioMap.values()) {
	  	                HSSFRow dataRow = sheet.createRow(rowNum);
	  	                dataRow.createCell(0).setCellValue(itemRelatorio.getIdItem());
	  	                dataRow.createCell(1).setCellValue(itemRelatorio.getNome());
	  	                dataRow.createCell(2).setCellValue(itemRelatorio.getQuantidade());
	  	                qntdTotalItens += itemRelatorio.getQuantidade();
	  	                
	  	                dataRow.createCell(3).setCellValue(itemRelatorio.getMediaTempoEntrega());
	  	                dataRow.createCell(4).setCellValue(itemRelatorio.getValorTotal());
	  	                valorTotalItens += itemRelatorio.getValorTotal();
	  	                
	  	                rowNum++;
	  	            }
	  	            HSSFRow dataRowVT = sheet.createRow(rowNum);
	  	            dataRowVT.createCell(2).setCellValue(qntdTotalItens);
	  	            dataRowVT.createCell(4).setCellValue(valorTotalItens);
	  	            
	  	       
	  	            //Aba de Pedidos cancelados
	  	       
	  	     List<PedidoModel> pedidosCancelados = pedidoRepository.findPedidosEntreDatas(Arrays.asList
	  	        		  												(StatusModel.ENTREGUE, StatusModel.PRONTO_ENTREGA, StatusModel.EM_PREPARACAO),
	  	        		  												startDate, now);
	  	        
	  	      Map<String, ItemRelatorio> itemRelatorioMap2 = new HashMap<>();

	  	        for (PedidoModel pedido : pedidosCancelados) {
	  	            String itemName = pedido.getItem().getNome();
	  	            ItemRelatorio itemRelatorio = itemRelatorioMap2.getOrDefault(itemName, new ItemRelatorio(itemName));
	  	            itemRelatorio.setIdItem(pedido.getItem().getId());
	  	            itemRelatorio.incrementarQuantidade(pedido.getQuantidade());
	  	            itemRelatorio.adicionarValorTotal(pedido.getValor());
	  	            itemRelatorioMap2.put(itemName, itemRelatorio);
	  	        }
	  	        
	  	        HSSFSheet sheet2 = workbook.createSheet("Pedidos Cancelados");
	  	        HSSFRow row2 = sheet2.createRow(0);
	  	        
	  	        // Adicionar cabeçalhos
	  	        	row2.createCell(0).setCellValue("ID");
	  	            row2.createCell(1).setCellValue("Item");
	  	            row2.createCell(2).setCellValue("Quantidade de Pedidos");
	  	            row2.createCell(3).setCellValue("Valor Total Perdido (R$)");

	  	            // Adicionar dados
	  	            int rowNum2 = 1;
	  	            double valorTotalItens2 = 0;
	  	            int qntdTotalItens2 = 0;
	  	            
	  	            for (ItemRelatorio itemRelatorio : itemRelatorioMap2.values()) {
	  	                HSSFRow dataRow = sheet2.createRow(rowNum2);
	  	                dataRow.createCell(0).setCellValue(itemRelatorio.getIdItem());
	  	                dataRow.createCell(1).setCellValue(itemRelatorio.getNome());
	  	                dataRow.createCell(2).setCellValue(itemRelatorio.getQuantidade());
	  	                qntdTotalItens2 += itemRelatorio.getQuantidade();
	  	                
	  	                dataRow.createCell(3).setCellValue(itemRelatorio.getValorTotal());
	  	                valorTotalItens2 += itemRelatorio.getValorTotal();
	  	                
	  	                rowNum2++;
	  	            }
	  	            HSSFRow dataRowVT2 = sheet2.createRow(rowNum2);
	  	            dataRowVT2.createCell(2).setCellValue(qntdTotalItens2);
	  	            dataRowVT2.createCell(3).setCellValue(valorTotalItens2);
	  	            
	  	            
	  	            ServletOutputStream ops = response.getOutputStream();
	  	            workbook.write(ops);
	  	            workbook.close();
	  	            ops.close();
	  	            
	        }catch (FileNotFoundException e) {
					log.error("Arquivo não encontrado");
				}catch (IOException e) {
					log.error("Erro ao processar arquivo");
				}
		        log.info("Arquivo Gerado com Sucesso");
	    }
}
