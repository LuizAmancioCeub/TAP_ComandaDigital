package com.comandadigital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.CaixaModel;

@Repository
public interface CaixaRepository extends JpaRepository<CaixaModel, Integer> {
	//consultarLogin
		UserDetails findByLogin(String login);
		
		UserDetails findBySenha(String senha);
}
