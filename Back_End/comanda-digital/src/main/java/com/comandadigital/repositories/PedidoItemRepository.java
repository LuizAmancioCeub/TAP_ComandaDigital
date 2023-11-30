//package com.comandadigital.repositories;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import com.comandadigital.models.PedidoModel;
//
//public interface PedidoItemRepository extends JpaRepository<PedidoModel, Integer> {
//	@Query("SELECT pedido FROM PedidoItemModel pedido WHERE pedido.id.pedido.comanda.cliente.login = :cpf")
//	List<PedidoModel>findPedidoByCpf(@Param("cpf") String cpf);
//}
