package com.comada.cardapio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.comada.cardapio.entity.Categoria;

// responsavel pela conexao com o banco
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
