package com.comandadigital.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.CategoriaModel;
import com.comandadigital.models.ItemModel;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, Integer>{
	
	boolean existsByNome(@Param("nome")String nome);
	
	@Query("SELECT item FROM ItemModel item WHERE item.categoria = :categoria AND item.status.id = 1")
	List<ItemModel> findItemByCategoria(@Param("categoria") CategoriaModel categoria);
}
