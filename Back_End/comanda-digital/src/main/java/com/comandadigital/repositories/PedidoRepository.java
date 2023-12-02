package com.comandadigital.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.PedidoModel;
import com.comandadigital.models.StatusModel;

import jakarta.transaction.Transactional;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Integer>{
	
	@Query("SELECT pedido FROM PedidoModel pedido WHERE pedido.comanda.cliente.login = :cpf")
	List<PedidoModel>findPedidoByCpf(@Param("cpf") String cpf);
	
	@Query("SELECT pedido FROM PedidoModel pedido WHERE pedido.comanda.cliente.login = :cpf AND pedido.status.id IN :statusId")
	List<PedidoModel>findPedidoByCpfAndStatus(@Param("cpf") String cpf, @Param("statusId")List<Integer> statusList);
	
	 @Query("SELECT MAX(p.id) FROM PedidoModel p")
	 Integer findMaxId();
	 
	 @Query("SELECT pedido FROM PedidoModel pedido WHERE pedido.status.id IN :statusId")
		List<PedidoModel>findPedidoByStaus(@Param("statusId")List<Integer> statusList);
	 
	 
	 
}
