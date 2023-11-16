package com.comandadigital.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.PedidoModel;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Integer>{
	
	@Query("SELECT pedido FROM PedidoModel pedido WHERE pedido.comanda.cliente.login = :cpf")
	List<PedidoModel>findPedidoByCpf(@Param("cpf") String cpf);
	
	@Query("SELECT pedido FROM PedidoModel pedido WHERE pedido.comanda.cliente.login = :cpf AND pedido.status.id = :statusId")
	List<PedidoModel>findPedidoByCpfAndStatus(@Param("cpf") String cpf, @Param("statusId")Integer statusId);
}
