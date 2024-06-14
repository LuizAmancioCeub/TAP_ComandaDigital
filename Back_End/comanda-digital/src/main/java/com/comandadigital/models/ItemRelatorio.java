package com.comandadigital.models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ItemRelatorio {
	private int idItem;
	  private String nome;
      private int quantidade;
      private long totalTempoEntrega;
      private double valorTotal;
      private int countTempoEntrega;

      public ItemRelatorio(String nome) {
          this.nome = nome;
      }

      public void incrementarQuantidade(int quantidade) {
          this.quantidade += quantidade;
      }

      public void adicionarTempoEntrega(LocalDateTime horarioPedido, LocalDateTime horarioEntrega) {
          if (horarioEntrega != null && horarioPedido != null) {
              this.totalTempoEntrega += ChronoUnit.MINUTES.between(horarioPedido, horarioEntrega);
              this.countTempoEntrega++;
          }
      }

      public void adicionarValorTotal(double valor) {
          this.valorTotal += valor;
      }
      
    public int getIdItem() {
		return idItem;
	}

	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}

	public String getNome() {
          return nome;
      }

      public int getQuantidade() {
          return quantidade;
      }

      public double getMediaTempoEntrega() {
          return countTempoEntrega > 0 ? (double) totalTempoEntrega / countTempoEntrega : 0;
      }

      public double getValorTotal() {
          return valorTotal;
      }
 }

