package com.comandadigital.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.ComandaModel;

@Repository
public interface ComandaRepository extends JpaRepository<ComandaModel, Integer> {
	
	@Query("SELECT comanda FROM ComandaModel comanda WHERE comanda.cliente.login = :login AND comanda.status.id IN :statusId")
	ComandaModel findComandaByCpf(@Param("login") String login, @Param("statusId") List<Integer> statusId);
}
