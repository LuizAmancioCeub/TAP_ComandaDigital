package com.comandadigital.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.comandadigital.dtos.CategoriaRecordDTO;
import com.comandadigital.dtos.myValidations.CategoriaUnique;
import com.comandadigital.dtos.myValidations.CustomUniqueConstraintViolationException;
import com.comandadigital.models.CategoriaModel;
import com.comandadigital.repositories.CategoriaRepository;

@Service
public class CategoriaServiceImplements implements CategoriaService {
	@Autowired
	CategoriaRepository categoriaRepository;
	
	@Override
	public boolean existsByCategoria(String categoria) {
		 return categoriaRepository.existsByCategoria(categoria);
	}

	@Override
	public Optional<CategoriaModel> findById(Integer id) {
        
		return categoriaRepository.findById(id);
    }
	
	@Override
	public CategoriaModel register(@CategoriaUnique  CategoriaRecordDTO categoriaDTO) {
		
		var categoriaModel = new CategoriaModel();
		BeanUtils.copyProperties(categoriaDTO, categoriaModel); // convertendo dto para model
		
		return categoriaRepository.save(categoriaModel);
	}


	@Override
	public List<CategoriaModel> findAll() {
		return categoriaRepository.findAll();
	}

	

	@Override
	public CategoriaModel update(Integer id, @CategoriaUnique CategoriaRecordDTO categoriaDTO) {
		 
		Optional<CategoriaModel> categoria0 = categoriaRepository.findById(id);
		 
		 if (categoria0.isEmpty()) {
	            return null;
	     }
		 
		 var CategoriaModel = categoria0.get();
		 BeanUtils.copyProperties(categoriaDTO, CategoriaModel); // convertendo dto para model
		 return categoriaRepository.save(CategoriaModel);
	}

	@Override
	public String delete(Integer id) {
		
		Optional<CategoriaModel> categoria0 = categoriaRepository.findById(id);
		
		if (categoria0.isEmpty()) {
            return "Categoria não encontrada";
        }
		
		CategoriaModel categoriaDelete = categoria0.get();
 		String name = categoriaDelete.getCategoria();
 		try {
 			categoriaRepository.delete(categoria0.get());
 		}catch (DataIntegrityViolationException e) {
 			throw new CustomUniqueConstraintViolationException("Categoria não pode ser excluída, existem itens vinculados");
		}
 		return "Categoria "+name+" deletada com Sucesso";
	}

}
