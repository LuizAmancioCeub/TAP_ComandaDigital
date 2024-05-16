package com.comandadigital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.GerenteModel;

@Repository
public interface GerenteRepository extends JpaRepository<GerenteModel, Integer> {
		//consultarLogin
		UserDetails findByLogin(String login);
		
		UserDetails findByCpf(String cpf);
		
		UserDetails findByEmail(String email);
		
		UserDetails findBySenha(String senha);
		
		@Query("SELECT MAX(CAST(SUBSTRING(g.login, 3) AS INTEGER)) FROM GerenteModel g")
		Long findMaxMatricula();
		
}
