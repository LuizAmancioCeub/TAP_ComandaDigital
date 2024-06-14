package com.comandadigital.repositories;

import java.time.LocalDateTime;
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
	
	@Query("SELECT pedido FROM PedidoModel pedido WHERE pedido.comanda.cliente.login = :cpf ORDER BY pedido.horarioPedido DESC ")
	List<PedidoModel>findPedidoByCpf(@Param("cpf") String cpf);
	
	@Query("SELECT pedido FROM PedidoModel pedido WHERE pedido.comanda.cliente.login = :cpf AND pedido.comanda.id = :idComanda ORDER BY pedido.horarioPedido ASC ")
	List<PedidoModel>findPedidoByCpfAndComanda(@Param("cpf") String cpf, Integer idComanda);
	
	@Query("SELECT pedido FROM PedidoModel pedido WHERE pedido.comanda.cliente.login = :cpf AND pedido.status.id IN :statusId")
	List<PedidoModel>findPedidoByCpfAndStatus(@Param("cpf") String cpf, @Param("statusId")List<Integer> statusList);
	
	@Query("SELECT pedido FROM PedidoModel pedido WHERE pedido.comanda.id = :idComanda AND pedido.status.id IN :statusId")
	List<PedidoModel>findPedidoByComandaAndStatus(@Param("idComanda") Integer id, @Param("statusId")List<Integer> statusList);
	
	 @Query("SELECT MAX(p.id) FROM PedidoModel p")
	 Integer findMaxId();
	 
	 @Query("SELECT pedido FROM PedidoModel pedido WHERE pedido.status.id IN :statusId")
		List<PedidoModel>findPedidoByStaus(@Param("statusId")List<Integer> statusList);
	 

	    @Query("SELECT pedido FROM PedidoModel pedido "
	         + "WHERE pedido.comanda.cliente.login = :cpf "
	         + "AND pedido.status.id IN :statusList "
	         + "AND pedido.comanda.status.id NOT IN :comandaStatusId")
	    List<PedidoModel> findPedidoByCpfAndStatusAndComandaStatus(
	        @Param("cpf") String cpf, 
	        @Param("statusList") List<Integer> statusList, 
	        @Param("comandaStatusId") Integer comandaStatusId);
	    
	    
	    @Query("SELECT p FROM PedidoModel p WHERE p.status.id NOT IN :status AND p.horarioPedido BETWEEN :startDate AND :endDate")
	    List<PedidoModel> findPedidosEntreDatas(@Param("status")List<Integer> status,@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
	 
}
