package com.comandadigital.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.PerfilModel;

@Repository
public interface PerfilRepository extends JpaRepository<PerfilModel, Integer>  {
	
	@Query("SELECT p FROM PerfilModel p WHERE id IN :perfilId")
	List<PerfilModel> findPerfilById(@Param("perfilId")List<Integer> perfilId);
	
}
