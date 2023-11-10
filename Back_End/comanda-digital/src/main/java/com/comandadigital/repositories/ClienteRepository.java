package com.comandadigital.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.ClienteModel;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, UUID> {
	
	//consultarLogin
	UserDetails findByCpf(String cpf);
	
	//consultar telefone
	UserDetails findByTelefone(String telefone);
	
	UserDetails findBySenha(String senha);
}