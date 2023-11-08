package com.comandadigital.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comandadigital.controllers.MesaController;
import com.comandadigital.controllers.StatusController;
import com.comandadigital.dtos.MesaRecordDTO;
import com.comandadigital.dtos.myValidations.MesaUnique;
import com.comandadigital.models.MesaModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.repositories.MesaRepository;
import com.comandadigital.repositories.StatusRepository;

@Service
public class MesaServiceImplements implements MesaService {
	@Autowired
	MesaRepository mesaRepository;
	@Autowired
	StatusRepository statusRepository;

	@Override
	public List<MesaModel> findAll() {
		List<MesaModel> mesas = mesaRepository.findAll();
		
		if(!mesas.isEmpty()) {
			
			for(MesaModel mesa : mesas) {
				Integer id = mesa.getId();
				mesa.add(linkTo(methodOn(MesaController.class).getOneMesaId(id)).withSelfRel());
				
				//mesa.add(linkTo(methodOn(MesaController.class).updateMesa(id,null)).withRel("editar_mesa"));
				
				//mesa.add(linkTo(methodOn(MesaController.class).deleteMesa(id)).withRel("deletar_mesa"));
				
				if(mesa.getStatus().getLinks().isEmpty()) {
					mesa.getStatus().add(linkTo(methodOn(StatusController.class).getAllStatus()).withSelfRel());
				}
			}
		}
		return mesas;
	}

	@Override
	public Optional<MesaModel> findById(Integer id) {
		Optional<MesaModel> mesa = mesaRepository.findById(id);
		if(mesa.isEmpty()) {
			return null;
		}
		mesa.get().getStatus().add(linkTo(methodOn(StatusController.class).getAllStatus()).withSelfRel());
		return mesa;
	}

	@Override
	public MesaModel register(@MesaUnique MesaRecordDTO mesaDTO) {
		var mesaModel = new MesaModel();
		BeanUtils.copyProperties(mesaDTO, mesaModel);
		
		// consultando o status inicial do item
		StatusModel defaultStatus = statusRepository.findById(9).orElse(null);
		
		mesaModel.setStatus(defaultStatus); // salvando o item já com status = 9(Livre)
		mesaModel.setQr_code("comanda-digital.com/"+mesaModel.getId()); // passando link para gerar QrCode
		
		return mesaRepository.save(mesaModel);
	}

	@Override
	public MesaModel update(Integer id, MesaRecordDTO mesaDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean existsById(Integer id) {
		return mesaRepository.existsById(id);
	}
}
