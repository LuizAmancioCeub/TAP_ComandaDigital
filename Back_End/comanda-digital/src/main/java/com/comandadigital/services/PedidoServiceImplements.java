package com.comandadigital.services;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.comandadigital.dtos.PedidoRecordDTO;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.ComandaModel;
import com.comandadigital.models.CozinhaModel;
import com.comandadigital.models.ItemModel;
import com.comandadigital.models.PedidoModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.ComandaRepository;
import com.comandadigital.repositories.CozinhaRepository;
import com.comandadigital.repositories.ItemRepository;
import com.comandadigital.repositories.PedidoRepository;
import com.comandadigital.repositories.StatusRepository;

@Service
public class PedidoServiceImplements implements PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
    private ItemRepository itemRepository;
	@Autowired
    private StatusRepository statusRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private ComandaRepository comandaRepository;
	@Autowired 
	private CozinhaRepository cozinhaRepository;
	
	@Override
	public PedidoModel register( PedidoRecordDTO pedidoDTO) {
		var pedidoModel = new PedidoModel();
		BeanUtils.copyProperties(pedidoDTO, pedidoModel);
		
		 // Obtém o contexto de segurança
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    // Obtém a autenticação do contexto de segurança
	    Authentication authentication = securityContext.getAuthentication();
	    // Verifica se a autenticação é do tipo UsernamePasswordAuthenticationToken
	    
	    if (authentication instanceof UsernamePasswordAuthenticationToken) {
	    	
	    	// Obtem detalhes do ClienteModel
	        ClienteModel clienteModel = (ClienteModel) authentication.getPrincipal();
	        // Obtém o CPF do cliente
	        String cpfDoUsuarioAutenticado = clienteModel.getLogin();
	        
	        // verificando comanda
	        ComandaModel comandaCliente = comandaRepository.findComandaByCpf(cpfDoUsuarioAutenticado,Arrays.asList(6, 7));
	        if(comandaCliente == null) {
	        	throw new RuntimeException("Comanda não encontrada para cpf "+cpfDoUsuarioAutenticado);
	        }
	        
	    	// Verificando se os itens do pedido existem e estão disponiveis
			Set<ItemModel> itens = pedidoModel.getItens();
			double valorPedido = 0;
			for(ItemModel item : itens) {
				ItemModel existingItem = itemRepository.findByIdAndStatusId(item.getId(), 1);
				if(existingItem == null) {
					throw new RuntimeException( item.getNome()+" não encontrado ou com status inválido");
				}
				valorPedido += existingItem.getPreco();
			}
			pedidoModel.setValor(valorPedido);
			
			// Setando status inicial do pedido
			StatusModel statusInicial = statusRepository.findById(3).orElseThrow(() -> new RuntimeException("Status não encontrado")); 
			pedidoModel.setStatus(statusInicial);
			
			CozinhaModel cozinha = cozinhaRepository.findById(1).orElseThrow(() -> new RuntimeException("Cozinha não encontrada"));
			pedidoModel.setCozinha(cozinha);
			
			pedidoModel.setComanda(comandaCliente);
			
			return pedidoRepository.save(pedidoModel);
	    }
		return null;
	}

	@Override
	public List<PedidoModel> findAll() {
		List<PedidoModel> pedidos = pedidoRepository.findAll();
		if(pedidos.isEmpty()) {
			throw new RuntimeException(" Não foram encontrados pedidos");
		}
		
//		for(PedidoModel pedido : pedidos) {
//			Integer id = pedido.getId();
//			
//			// Adicione um link para acessar as informações detalhadas do pedido (self)
//			pedido.add(linkTo(methodOn(PedidoController.class).getOnePedidoId(id)).withSelfRel());
//								
//			// Adicione um link para a operação de edição do pedido pelo gerente
//			pedido.add(linkTo(methodOn(PedidoController.class).updatePedido(id,null)).withRel("editar_pedido"));
//								
//			// Adicione um link para a operação de exclusão do pedido pelo gerente
//			pedido.add(linkTo(methodOn(PedidoController.class).deletePedido(id)).withRel("deletar_pedido"));
//								
//			if(pedido.getStatus().getLinks().isEmpty()) {
//				pedido.getStatus().add(linkTo(methodOn(StatusController.class).getAllStatus()).withSelfRel());
//			}
//		}
		
		return pedidos;
	}

//	@Override
//	public List<PedidoModel> findPedidoByCpf(String cpf) {
//		UserDetails cliente = clienteRepository.findByCpf(cpf);
//		if (cliente == null) {
//	        throw new RuntimeException("Cliente não encontrado");
//	    }
//		return pedidoRepository.findPedidosByCpf(cpf);
//	}
	
	public List<PedidoModel> findMyPedidos(){
		 
		// Obtém o contexto de segurança
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    // Obtém a autenticação do contexto de segurança
	    Authentication authentication = securityContext.getAuthentication();
	 // Verifica se a autenticação é do tipo UsernamePasswordAuthenticationToken
	    if (authentication instanceof UsernamePasswordAuthenticationToken) {
	    	// Obtem detalhes do ClienteModel
	        ClienteModel clienteModel = (ClienteModel) authentication.getPrincipal();
	        // Obtém o CPF do cliente
	        String cpfDoUsuarioAutenticado = clienteModel.getLogin();
	        
	        List<PedidoModel> pedidosCliente = pedidoRepository.findPedidoByCpf(cpfDoUsuarioAutenticado);
	        
	        return pedidosCliente;
	    } 
	    
	    return null;
	}

	@Override
	public PedidoModel update(Integer id, PedidoRecordDTO pedidoDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
