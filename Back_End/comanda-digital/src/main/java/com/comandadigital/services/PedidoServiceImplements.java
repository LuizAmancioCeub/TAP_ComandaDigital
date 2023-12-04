package com.comandadigital.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.comandadigital.dtos.PedidoRecordDTO;
import com.comandadigital.dtos.PedidoRecordUpdateDTO;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.ComandaModel;
import com.comandadigital.models.CozinhaModel;
import com.comandadigital.models.ItemModel;
//import com.comandadigital.models.PedidoItemModel;
//import com.comandadigital.models.PedidoItemPK;
import com.comandadigital.models.PedidoModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.ComandaRepository;
import com.comandadigital.repositories.CozinhaRepository;
import com.comandadigital.repositories.ItemRepository;
import com.comandadigital.repositories.PedidoRepository;
import com.comandadigital.repositories.StatusRepository;

import jakarta.transaction.Transactional;

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
	@Autowired
	private ComandaServiceImplements comandaService;
//	@Autowired
//	private PedidoItemRepository pedidoItemRepository;
	
//	@Override
//	public PedidoModel register( PedidoRecordDTO pedidoDTO) {
//		var pedidoModel = new PedidoModel();
//		BeanUtils.copyProperties(pedidoDTO, pedidoModel);
//		
//		 // Obtém o contexto de segurança
//	    SecurityContext securityContext = SecurityContextHolder.getContext();
//	    // Obtém a autenticação do contexto de segurança
//	    Authentication authentication = securityContext.getAuthentication();
//	    
//	    if (authentication instanceof UsernamePasswordAuthenticationToken) {
//	    	
//	    	// Obtem detalhes do ClienteModel
//	        ClienteModel clienteModel = (ClienteModel) authentication.getPrincipal();
//	        // Obtém o CPF do cliente
//	        String cpfDoUsuarioAutenticado = clienteModel.getLogin();
//	        
//	        // verificando comanda
//	        ComandaModel comandaCliente = comandaRepository.findComandaByCpf(cpfDoUsuarioAutenticado,Arrays.asList(8, 9));
//	        if(comandaCliente == null) {
//	        	throw new RuntimeException("Comanda não encontrada para cpf "+cpfDoUsuarioAutenticado);
//	        }
//	        
//	        // Verificando se os itens do pedido existem e estão disponíveis
//	        Set<PedidoItemModel> itens = pedidoDTO.itens();
//	        double valorPedido = 0;
//	      
//			
//	        for(PedidoItemModel pedidoItem : itens) {
//				
//				var existingItem = itemRepository.findByIdAndStatusId(pedidoItem.getItem().getId(), 1);	
//				
//				if(existingItem == null) {
//					return null;
//				}
//			}
			
	
	@Override
	public PedidoModel register( PedidoRecordDTO pedidoDTO) {
		var pedidoModel = new PedidoModel();
		BeanUtils.copyProperties(pedidoDTO, pedidoModel);
		
		 // Obtém o contexto de segurança
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    // Obtém a autenticação do contexto de segurança
	    Authentication authentication = securityContext.getAuthentication();
	    
	    if (authentication instanceof UsernamePasswordAuthenticationToken) {
	    	
	    	// Obtem detalhes do ClienteModel
	        ClienteModel clienteModel = (ClienteModel) authentication.getPrincipal();
	        // Obtém o CPF do cliente
	        String cpfDoUsuarioAutenticado = clienteModel.getLogin();
	        
	        // verificando comanda
	        ComandaModel comandaCliente = comandaRepository.findComandaByCpf(cpfDoUsuarioAutenticado,Arrays.asList(8, 9));
	        if(comandaCliente == null) {
	        	return null;
	        }
	        
	        // Verificando se os itens do pedido existem e estão disponíveis
	        ItemModel item = pedidoDTO.item();
	        var existingItem = itemRepository.findByIdAndStatusId(item.getId(), 1);	
				
			if(existingItem == null) {
				return null;
			}
			
		     pedidoModel.setItem(existingItem);	
		     pedidoModel.setQuantidade(pedidoDTO.quantidade());
		     pedidoModel.setObservacao(pedidoDTO.observacao());
		     pedidoModel.setValor(existingItem.getPreco() * pedidoModel.getQuantidade());
		     // Setando status inicial do pedido
		     StatusModel statusInicial = statusRepository.findById(3).orElseThrow(() -> new RuntimeException("Status não encontrado")); 
		     pedidoModel.setStatus(statusInicial);
		     									
		     CozinhaModel cozinha = cozinhaRepository.findById(1).orElseThrow(() -> new RuntimeException("Cozinha não encontrada"));
		     pedidoModel.setCozinha(cozinha);
		     									
		     pedidoModel.setComanda(comandaCliente);
		     return  pedidoRepository.save(pedidoModel);	
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
	
	public List<PedidoModel> findMyPedidosEmPreparo(){
		List<Integer> statusList = Arrays.asList(3, 4);
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
	        
	        List<PedidoModel> pedidosCliente = pedidoRepository.findPedidoByCpfAndStatus(cpfDoUsuarioAutenticado,statusList);
	        if(pedidosCliente == null) {
	        	return null;
	        }
	        return pedidosCliente;
	    } 
	    
	    return null;
	}
	
	public List<PedidoModel> findMyPedidosEntregues(){
		List<Integer> statusList = Arrays.asList(5);
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
	        
	        List<PedidoModel> pedidosCliente = pedidoRepository.findPedidoByCpfAndStatus(cpfDoUsuarioAutenticado, statusList);
	        if(pedidosCliente == null) {
	        	return null;
	        }
	        
	        return pedidosCliente;
	    } 
	    
	    return null;
	}


	@Override
	public PedidoModel update(Integer id, PedidoRecordUpdateDTO dto) {
		PedidoModel pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
		
		if(pedido.getStatus().getId() == 3) {
			  if (!Objects.equals(dto.quantidade(), pedido.getQuantidade()) && !Objects.equals(dto.valor(), pedido.getValor())) {
			        pedido.setValor(dto.valor());
			        pedido.setQuantidade(dto.quantidade());
			    }
	    if (!Objects.equals(dto.observacao(), pedido.getObservacao())) {
			        pedido.setObservacao(dto.observacao());
	    }
	    
			return pedidoRepository.save(pedido);
		}
		
		return null ;
	}
	
	public void updateStatus(Integer id, Integer statusNovo) {
		
		PedidoModel pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
		StatusModel status = statusRepository.findById(statusNovo).orElseThrow(() -> new RuntimeException("Status não encontrado com ID: " + id));
		pedido.setStatus(status);
		if(status.getId() == 5) {
			pedido.setHorarioEntrega(LocalDateTime.now());	}
		pedidoRepository.save(pedido);
		if(pedido.getStatus().getId() == 5 ) {
			var valor = pedido.getValor();
			BigDecimal v = BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_EVEN);
			valor = v.doubleValue();
			comandaService.updateValor(pedido.getComanda().getId(), valor); 
		}
	}
	
	@Transactional
	public void updateValor(Integer id, double newValor) {
		PedidoModel pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
		
		 BigDecimal v = BigDecimal.valueOf(newValor).setScale(2, RoundingMode.HALF_EVEN);
		 newValor = v.doubleValue();
        // Atualiza o valor do pedido
        pedido.setValor(newValor);

        // Salva as alterações no banco de dados
        pedidoRepository.save(pedido);
	}

	@Override
	public String delete(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<PedidoModel> findPedidosByCpf(String cpf, List<Integer> statusId) {
		UserDetails clienteDetails  = clienteRepository.findByLogin(cpf);
		if(clienteDetails == null) {
			return null;
		}
		 return pedidoRepository.findPedidoByCpf(cpf);
	}
	
	public List<PedidoModel> findPedidosByStatus(List<Integer> statusId) {
		
		 return pedidoRepository.findPedidoByStaus(statusId);
	}

}
