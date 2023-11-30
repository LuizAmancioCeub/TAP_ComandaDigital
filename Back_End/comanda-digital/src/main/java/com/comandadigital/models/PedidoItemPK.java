//package com.comandadigital.models;
//
//import java.io.Serializable;
//
//import jakarta.persistence.Embeddable;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@EqualsAndHashCode
//
//@Embeddable
//public class PedidoItemPK implements Serializable {
//	 private static final long serialVersionUID = 1L;
//
//	    @ManyToOne
//	    @JoinColumn(name = "NU_PEDIDO")
//	    private PedidoModel pedido;
//
//	    @ManyToOne
//	    @JoinColumn(name = "NU_ITEM")
//	    private ItemModel item;
//	    
//	    // Adicione este construtor
//	    public PedidoItemPK(PedidoModel pedido, ItemModel item) {
//	        this.pedido = pedido;
//	        this.item = item;
//	    }
//	    public PedidoItemPK() {}
//}
