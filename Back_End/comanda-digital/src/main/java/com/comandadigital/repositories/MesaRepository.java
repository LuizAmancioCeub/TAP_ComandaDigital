package com.comandadigital.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.MesaModel;

@Repository
public interface MesaRepository extends JpaRepository<MesaModel, Integer> {
	
	@Query("SELECT cliente FROM ClienteModel cliente WHERE cliente.mesa.id = :mesaId")
	List<ClienteModel> findClienteByMesa(@Param("mesaId") Integer mesaId);
}
