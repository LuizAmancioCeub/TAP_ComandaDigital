package com.comandadigital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.PerfilModel;

@Repository
public interface PerfilRepository extends JpaRepository<PerfilModel, Integer>  {

}
