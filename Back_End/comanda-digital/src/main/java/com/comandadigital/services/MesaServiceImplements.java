package com.comandadigital.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comandadigital.controllers.MesaController;
import com.comandadigital.controllers.StatusController;
import com.comandadigital.dtos.MesaRecordDTO;
import com.comandadigital.dtos.myValidations.MesaUnique;
import com.comandadigital.dtos.myValidations.Exceptions.NegocioException;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.MesaModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.models.projection.ClienteProjection;
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
			throw new NegocioException("Mesa não encontrada");
		}
		mesa.get().getStatus().add(linkTo(methodOn(StatusController.class).getAllStatus()).withSelfRel());
		return mesa;
	}

	@Override
	@Transactional
	public MesaModel register(@MesaUnique MesaRecordDTO mesaDTO) {
		var mesaModel = new MesaModel();
		BeanUtils.copyProperties(mesaDTO, mesaModel);
		
		// consultando o status inicial do item
		StatusModel defaultStatus = statusRepository.findById(StatusModel.LIVRE).orElse(null);
		
		mesaModel.setStatus(defaultStatus); // salvando o item já com status = 9(Livre)
		mesaModel.setQr_code("comanda-digital.com/"+mesaModel.getId()); // passando link para gerar QrCode
		
		return mesaRepository.save(mesaModel);
	}

	@Override
	@Transactional
	public MesaModel update(Integer id, MesaRecordDTO mesaDTO) throws Exception {
		Optional<MesaModel> mesa0 = mesaRepository.findById(id);
		
		if(mesa0.isEmpty()) {
			throw new NegocioException("Mesa não encontrada");
		}
		StatusModel status = mesaDTO.status();
		Optional<StatusModel> existingMesa = statusRepository.findById(status.getId());
		if(existingMesa.isEmpty()) {
			throw new NegocioException("Mesa não encontrada");
		}
		var mesaModel = mesa0.get();
		BeanUtils.copyProperties(mesaDTO, mesaModel);
		try {
			return mesaRepository.save(mesaModel);
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	@Transactional
	public String delete(Integer id) {
		Optional<MesaModel> mesa0 = mesaRepository.findById(id);
		if(mesa0.isEmpty()) {
			throw new RuntimeException("Mesa não encontrada");
		}
		if(mesa0.get().getStatus().getId().equals(StatusModel.OCUPADA)||mesa0.get().getStatus().getId().equals(StatusModel.RESERVADA)) {
			throw new NegocioException("Mesa "+id+" ocupada, tente novamente quando estiver com status LIVRE");
		}
		MesaModel mesaDelete = mesa0.get();
		mesaRepository.delete(mesaDelete);
		return "Mesa "+mesaDelete.getId()+"deletado";
		
	}
	
	public boolean existsById(Integer id) {
		return mesaRepository.existsById(id);
	}
	
	@Transactional
	 public void atualizarStatusMesa(Integer mesaId, Integer nuStatusAntigo, Integer nuStatusNovo) throws Exception {
	        Optional<MesaModel> mesaOptional = mesaRepository.findById(mesaId);
	        if (mesaOptional.isPresent() && mesaOptional.get().getStatus().getId().equals(nuStatusAntigo)) {
	            // Lógica de atualização da mesa para o status desejado 
	        	if(nuStatusNovo.equals(StatusModel.INDISPONIVEL) &&
	        			(nuStatusAntigo.equals(StatusModel.OCUPADA) || nuStatusAntigo.equals(StatusModel.RESERVADA))) {
	        		throw new NegocioException("Mesa "+mesaId+" ocupada, tente novamente quando estiver com status LIVRE");
	        	}
	            StatusModel novoStatus = statusRepository.findById(nuStatusNovo).orElseThrow(() -> new RuntimeException("Status não encontrado"));
	            mesaOptional.get().setStatus(novoStatus);
	            try {
	            	mesaRepository.save(mesaOptional.get());
	            }catch (NegocioException e) {
	    			throw new NegocioException(e.getMessage());
	    		}catch (Exception e) {
					throw new Exception(e.getMessage());
				}
	        }
	 }
	
	public List<ClienteProjection> findClientesMesa(Integer id) {
		List<ClienteModel> clienteMesa = mesaRepository.findClienteByMesa(id);
		List<ClienteProjection> clienteMesa0 = new ArrayList<>();
		if(!clienteMesa.isEmpty()) {
			for (ClienteModel cliente : clienteMesa) {
				ClienteProjection projection = new ClienteProjection();
				projection.setNome(cliente.getNome());
				projection.setTelefone(cliente.getTelefone());
				projection.setLogin(cliente.getLogin());
				clienteMesa0.add(projection);
			}
			return clienteMesa0;
		}
		throw new NegocioException("Mesa sem clientes");
	}
}
