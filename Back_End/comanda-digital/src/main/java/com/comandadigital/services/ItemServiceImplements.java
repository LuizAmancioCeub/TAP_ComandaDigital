package com.comandadigital.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comandadigital.controllers.CategoriaController;
import com.comandadigital.controllers.ItemController;
import com.comandadigital.controllers.StatusController;
import com.comandadigital.dtos.ItemRecordDTO;
import com.comandadigital.dtos.myValidations.ItemUnique;
import com.comandadigital.models.CategoriaModel;
import com.comandadigital.models.ItemModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.repositories.CategoriaRepository;
import com.comandadigital.repositories.ItemRepository;
import com.comandadigital.repositories.StatusRepository;

@Service
public class ItemServiceImplements implements ItemService{
	
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	StatusRepository statusRepository;
	@Autowired
	CategoriaRepository categoriaRepository;
	
	@Override
	public List<ItemModel> findItemByCategoria(CategoriaModel categoria) {
		List<ItemModel> itens = itemRepository.findItemByCategoria(categoria);
		
		if(!itens.isEmpty()) {
			
			for(ItemModel item : itens) {
				Integer id = item.getId();
				
				// Adicione um link para acessar as informações detalhadas do produto (self)
				item.add(linkTo(methodOn(ItemController.class).getOneItemId(id)).withSelfRel());
				
				// Adicione um link para a operação de edição do produto
				item.add(linkTo(methodOn(ItemController.class).updateItem(id,null)).withRel("editar_item"));
				
				// Adicione um link para a operação de exclusão do produto
				item.add(linkTo(methodOn(ItemController.class).deleteItem(id)).withRel("deletar_item"));
				
				if(item.getCategoria().getLinks().isEmpty()) {
					item.getCategoria().add(linkTo(methodOn(CategoriaController.class).getOneCategoriaId(item.getCategoria().getId())).withSelfRel());	
				}
				
				if(item.getStatus().getLinks().isEmpty()) {
					item.getStatus().add(linkTo(methodOn(StatusController.class).getAllStatus()).withSelfRel());
				}
			}
		}
		return itens;
	}

	@Override
	public Optional<ItemModel> findById(Integer id) {
		Optional<ItemModel> item =  itemRepository.findById(id);
		if(item.isEmpty()) {
			return null;
		}
		
		item.get().getCategoria().add(linkTo(methodOn(CategoriaController.class).getOneCategoriaId(item.get().getCategoria().getId())).withSelfRel());
		item.get().getStatus().add(linkTo(methodOn(StatusController.class).getAllStatus()).withSelfRel());
		return item;
	}

	@Override
	public List<ItemModel> findAll() {
		List<ItemModel> itens = itemRepository.findAll();
		if(itens.isEmpty()) {
			return null;
		}
		
		
		
		for(ItemModel item : itens) {
			
			Integer id = item.getId();
					
			// Adicione um link para acessar as informações detalhadas do produto (self)
			item.add(linkTo(methodOn(ItemController.class).getOneItemId(id)).withSelfRel());
					
			// Adicione um link para a operação de edição do produto
			item.add(linkTo(methodOn(ItemController.class).updateItem(id,null)).withRel("editar_item"));
					
			// Adicione um link para a operação de exclusão do produto
			item.add(linkTo(methodOn(ItemController.class).deleteItem(id)).withRel("deletar_item"));
			
			if(item.getCategoria().getLinks().isEmpty()) {
				item.getCategoria().add(linkTo(methodOn(CategoriaController.class).getOneCategoriaId(item.getCategoria().getId())).withSelfRel());	
			}
			
			if(item.getStatus().getLinks().isEmpty()) {
				item.getStatus().add(linkTo(methodOn(StatusController.class).getAllStatus()).withSelfRel());
			}
			
		}
		return itens;
		
	}

	@Override
	public ItemModel register(@ItemUnique ItemRecordDTO itemDTO) {
		var itemModel = new ItemModel();
		BeanUtils.copyProperties(itemDTO, itemModel);
		
		// consultando o status inicial do item
		StatusModel defaultStatus = statusRepository.findById(1).orElse(null);
		itemModel.setStatus(defaultStatus); // salvando o item já com status = 1(Ativo)
		
		return itemRepository.save(itemModel);
	}

	@Override
	public ItemModel update(Integer id, @ItemUnique ItemRecordDTO itemDTO) {
		Optional<ItemModel> item0 = itemRepository.findById(id);
		
		if(item0.isEmpty()) {
			return null;
		}
		
		CategoriaModel categoria = itemDTO.categoria();
		Optional<CategoriaModel> existingCategoria = categoriaRepository.findById(categoria.getId());
		if (existingCategoria.isEmpty()) {
		    // Categoria não encontrada, retorne uma resposta com status 404 e uma mensagem informativa
			return null;
		}

		 var ItemModel = item0.get();

		 BeanUtils.copyProperties(itemDTO, ItemModel); // convertendo dto para model
		 return itemRepository.save(ItemModel);
	}

	@Override
	public String delete(Integer id) {
		Optional<ItemModel> item0 = itemRepository.findById(id);
		
		if (item0.isEmpty()) {
            return "Categoria não encontrada";
        }
		
		ItemModel itemDelete = item0.get();
 		String name = itemDelete.getNome();
 		
 		itemRepository.delete(item0.get());
 		return "Categoria "+name+" deletada com Sucesso";
	}


	@Override
	public boolean existsByNome(String nome) {
		return itemRepository.existsByNome(nome);
	}

}
