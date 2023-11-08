package com.comandadigital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.MesaModel;

@Repository
public interface MesaRepository extends JpaRepository<MesaModel, Integer> {

}
