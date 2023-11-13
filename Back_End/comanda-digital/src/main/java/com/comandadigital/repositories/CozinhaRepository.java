package com.comandadigital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.CozinhaModel;

@Repository
public interface CozinhaRepository extends JpaRepository<CozinhaModel, Integer> {
	
	//consultarLogin
	UserDetails findByTipo(String tipo);
	
	UserDetails findBySenha(String senha);
}
