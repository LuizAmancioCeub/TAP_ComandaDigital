package com.comandadigital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.comandadigital.models.StatusModel;

@Repository
public interface StatusRepository extends JpaRepository<StatusModel, Integer> {

}
