package com.comandadigital.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.CategoriaModel;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, Integer> {
	
	boolean existsByCategoria(@Param("categoria")String categoria);
	
	@Query("SELECT c FROM CategoriaModel c WHERE c.categoria LIKE %:categoria% Order By categoria ASC ")
	public List<CategoriaModel> findByName(@Param("categoria")String categoria); // poderia usar o findByNameContaining do JPA
}
