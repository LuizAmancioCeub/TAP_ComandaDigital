package com.comandadigital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.GerenteModel;

@Repository
public interface GerenteRepository extends JpaRepository<GerenteModel, Integer> {
		//consultarLogin
		UserDetails findByLogin(String login);
		
		UserDetails findBySenha(String senha);
}
