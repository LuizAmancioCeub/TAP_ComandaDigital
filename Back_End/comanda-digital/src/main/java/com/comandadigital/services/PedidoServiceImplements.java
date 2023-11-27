package com.comandadigital.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.comandadigital.models.PedidoItemModel;
import com.comandadigital.models.PedidoItemPK;
import com.comandadigital.models.PedidoModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.ComandaRepository;
import com.comandadigital.repositories.CozinhaRepository;
import com.comandadigital.repositories.ItemRepository;
import com.comandadigital.repositories.PedidoItemRepository;
import com.comandadigital.repositories.PedidoRepository;
import com.comandadigital.repositories.StatusRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class PedidoServiceImplements implements PedidoService {
	 @PersistenceContext
	 private EntityManager entityManager;
	
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
	private PedidoItemRepository pedidoItemRepository;
	
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
	        	throw new RuntimeException("Comanda não encontrada para cpf "+cpfDoUsuarioAutenticado);
	        }
	        
	        // Verificando se os itens do pedido existem e estão disponíveis
	        Set<PedidoItemModel> itens = pedidoDTO.itens();
	        double valorPedido = 0;
	      
			
	        for(PedidoItemModel pedidoItem : itens) {
				
				var existingItem = itemRepository.findByIdAndStatusId(pedidoItem.getItem().getId(), 1);	
				
				if(existingItem == null) {
					return null;
				}
			}
			
	     
	     					
	     // Setando status inicial do pedido
	     StatusModel statusInicial = statusRepository.findById(3).orElseThrow(() -> new RuntimeException("Status não encontrado")); 
	     pedidoModel.setStatus(statusInicial);
	     									
	     CozinhaModel cozinha = cozinhaRepository.findById(1).orElseThrow(() -> new RuntimeException("Cozinha não encontrada"));
	     pedidoModel.setCozinha(cozinha);
	     									
	     pedidoModel.setComanda(comandaCliente);
	     PedidoModel savedPedido = pedidoRepository.save(pedidoModel);	
	    	
			for(PedidoItemModel pedidoItem : itens) {
				var existingItem = itemRepository.findByIdAndStatusId(pedidoItem.getItem().getId(), 1);	
				
	            // Cria um novo ItemModel associado ao PedidoModel
	            PedidoItemModel pedidoItemModel = new PedidoItemModel();
	            pedidoItemModel.setId(new PedidoItemPK(savedPedido, existingItem));
	            pedidoItemModel.setQuantidade(pedidoItem.getQuantidade());
	            pedidoItemModel.setObservacao(pedidoItem.getObservacao());
	            pedidoItemModel.setValor(existingItem.getPreco()* pedidoItem.getQuantidade());
	            pedidoItemModel.setStatus(statusInicial);
	            
	            pedidoItemRepository.save(pedidoItemModel);
	            
	            //savedPedido.getItens().add(pedidoItemModel);
	            
				valorPedido += pedidoItemModel.getValor();
			}
			//savedPedido.setValor(valorPedido);
			this.updateValor(savedPedido.getId(), valorPedido);
						
			//pedidoItemRepository.saveAll(itensPedido);
			
			
			// Salvar o pedido atualizado com os itens associados
	        return savedPedido;

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
		
		return null ;
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

}
