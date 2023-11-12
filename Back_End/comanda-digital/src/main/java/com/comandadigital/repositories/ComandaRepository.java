package com.comandadigital.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.ComandaModel;

@Repository
public interface ComandaRepository extends JpaRepository<ComandaModel, Integer> {
	
	@Query("SELECT comanda FROM ComandaModel comanda WHERE comanda.cliente.cpf = :cpf AND comanda.status.id = :statusId")
	Optional<ComandaModel>findComandaByCpf(@Param("cpf") String cpf, @Param("statusId") Integer statusId);
}
