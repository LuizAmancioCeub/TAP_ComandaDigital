package com.comandadigital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.FeedbackModel;
import com.comandadigital.models.ItemModel;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackModel, Integer> {
	
	@Query("SELECT feedback FROM FeedbackModel feedback WHERE feedback.cliente = :cliente AND feedback.item = :item")
	FeedbackModel findByClienteAndItem(@Param("cliente") ClienteModel cliente, @Param("item")ItemModel item);

}
