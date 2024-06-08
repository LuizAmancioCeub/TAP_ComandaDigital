package com.comandadigital.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comandadigital.controllers.StatusController;
import com.comandadigital.dtos.ComandaRecordDTO;
import com.comandadigital.dtos.myValidations.Exceptions.NegocioException;
import com.comandadigital.models.ClienteModel;
import com.comandadigital.models.ComandaModel;
import com.comandadigital.models.MesaModel;
import com.comandadigital.models.PedidoModel;
import com.comandadigital.models.StatusModel;
import com.comandadigital.models.projection.ClienteProjection;
import com.comandadigital.models.projection.ComandaProjection;
import com.comandadigital.models.projection.PedidosProjection;
import com.comandadigital.repositories.ClienteRepository;
import com.comandadigital.repositories.ComandaRepository;
import com.comandadigital.repositories.MesaRepository;
import com.comandadigital.repositories.PedidoRepository;
import com.comandadigital.repositories.StatusRepository;

@Service
public class ComandaServiceImplements implements ComandaService {
	@Autowired
	ComandaRepository comandaRepository;
	@Autowired
	StatusRepository statusRepository;
	@Autowired
	PedidoRepository pedidoRepository;
	@Autowired
	ClienteRepository clienteRepository;
	@Autowired
	MesaRepository mesaRepository;
	
	// Falta colocar Hateoas
	
	@Override
	public Optional<ComandaModel> findById(Integer id) {
		Optional<ComandaModel> comanda = comandaRepository.findById(id);
		if(comanda.isEmpty()) {
			throw new RuntimeException("Comanda não encontrada");
		}
		
		comanda.get().getStatus().add(linkTo(methodOn(StatusController.class).getAllStatus()).withSelfRel());
		return comanda;
	}

	@Override
	public List<ComandaModel> findAll() {
		List<ComandaModel> comandas = comandaRepository.findAll();
		if(comandas.isEmpty()) {
			throw new RuntimeException(" Não foram encontrada Comandas");
		}
		
		return comandas;
	}
	
	@Override
	public ComandaModel register(ComandaRecordDTO comandaDTO) {
		var comandaModel = new ComandaModel();
		BeanUtils.copyProperties(comandaDTO, comandaModel);
		
		// consultando o status inicial do item
		StatusModel defaultStatus = statusRepository.findById(StatusModel.ABERTA).orElseThrow(() -> new RuntimeException("Status não encontrado"));
		comandaModel.setStatus(defaultStatus);
		// valor incial da comanda
		comandaModel.setValorTotal(0.0);
		comandaModel.setTs_atualizacao(LocalDateTime.now());
		return this.comandaRepository.save(comandaModel);
	}

	@Override
	public ComandaModel update(Integer id, ComandaRecordDTO comandaDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete(Integer id) {
		Optional<ComandaModel> comanda0 = comandaRepository.findById(id);
		if(comanda0.isEmpty()) {
			throw new RuntimeException("Comanda não encontrada");
		}
		ComandaModel comandaDelete = comanda0.get();
		comandaRepository.delete(comandaDelete);
		return "Comanda do cliente "+comandaDelete.getCliente().getNome()+
				" de cpf: "+comandaDelete.getCliente().getLogin()+" deletada com sucesso";
	}
	
	public ComandaProjection findComandaByCpf(String cpf, List<Integer> statusId){
		
		UserDetails clienteDetails  = clienteRepository.findByLogin(cpf);
		if(clienteDetails == null) {
			throw new NegocioException("Número de cpf não encontrado");
		}
		
		ComandaModel comandaModel = comandaRepository.findComandaByCpf(cpf, statusId);
		
		if(comandaModel == null) {
			comandaModel = comandaRepository.findComandaByCpf(cpf, Arrays.asList(StatusModel.PAGA));
		}
		if(comandaModel != null) {
			ClienteProjection cliente = new ClienteProjection(comandaModel.getCliente().getNome(), comandaModel.getCliente().getTelefone(),
					comandaModel.getCliente().getEmail(), comandaModel.getCliente().getLogin(), comandaModel.getCliente().getPerfil().getId() ); 
			
			int mesa = comandaModel.getCliente().getMesa().getId();
			String garcom = comandaModel.getCliente().getMesa().getGarcom().getNome();
			
			List<PedidoModel> pedidos = pedidoRepository.findPedidoByCpfAndComanda(cpf, comandaModel.getId());
			List<PedidosProjection> pedidos0 = new ArrayList<>();
			
			if(!pedidos.isEmpty()) {
				for(PedidoModel pedido : pedidos) {
					if(!pedido.getStatus().getId().equals(StatusModel.CANCELADO)) {
						PedidosProjection p = new PedidosProjection();
						p.setIdItem(pedido.getItem().getId());
						p.setNomeItem(pedido.getItem().getNome());
						p.setPrecoItem(pedido.getItem().getPreco());
						p.setQuantidade(pedido.getQuantidade());
						p.setValor(pedido.getValor());
						p.setStatus(pedido.getStatus());
						p.setHorarioPedido(pedido.getHorarioPedido());
						p.setHorarioEntrega(pedido.getHorarioEntrega());
						p.setGarcom(garcom);
						p.setComanda(comandaModel.getId());
						pedidos0.add(p);
					}
					
				}
			}
			ComandaProjection projection = new ComandaProjection(comandaModel.getId(),comandaModel.getValorTotal(),
					comandaModel.getStatus().getStatus(), cliente, mesa, garcom,pedidos0,formatarData(comandaModel.getDtAbertura()));
			
			return projection;
		}
		return new ComandaProjection();
	}
	
	// método para retornar comanda do cliente logado
	public ComandaProjection findMyComanda(){
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
	        
	        List<ComandaModel> comandas = comandaRepository.findComandasByCpf(cpfDoUsuarioAutenticado);
	        List<Integer> status = new ArrayList<Integer>(); 
	        // verifica se possui comanda não paga
	        for(ComandaModel c : comandas) {
	        	if(!c.getStatus().getId().equals(StatusModel.PAGA)) {
	        		 status.add(StatusModel.ABERTA);
	        	        status.add(StatusModel.AGUARDANDO_PAGAMENTO);
	        		break;
	        	}else {
	        		status.add(StatusModel.PAGA);
	        		break;
	        	}
	        }
	        
	        ComandaProjection comanda = findComandaByCpf(cpfDoUsuarioAutenticado,status);
	        
	        
	        if(comanda == null) {
	        	throw new RuntimeException("Comanda não encontrada para cpf "+cpfDoUsuarioAutenticado);
	        }
	        // hipermidia para buscar os pedidos
	       // comandaCliente.add(linkTo(methodOn(PedidoController.class).getPedidosByCpf(cpfDoUsuarioAutenticado)).withRel("pedidos"));
	        
	        return comanda; // retorna comanda o cliente logado

	    }
	    return null;
	}
	
	@Transactional
	public void updateValor(Integer id,double valor) {
		ComandaModel comanda = comandaRepository.findById(id).orElseThrow(() -> new RuntimeException("Comanda não encontrado com ID: " + id));
		
		var valorTotal = comanda.getValorTotal() + valor;
		BigDecimal v = BigDecimal.valueOf(valorTotal).setScale(2, RoundingMode.HALF_EVEN);
		valorTotal = v.doubleValue();
		
		comanda.setValorTotal(valorTotal);
		comanda.setTs_atualizacao(LocalDateTime.now());
		comandaRepository.save(comanda);
	}
	
	@Transactional
	public void updateStatus(Integer id, Integer newstatus) throws Exception {
		try {
			ComandaModel comanda = comandaRepository.findById(id).orElseThrow(() -> new RuntimeException("Comanda não encontrado com ID: " + id));
			StatusModel status = statusRepository.findById(newstatus).orElseThrow(() -> new NegocioException("Status não encontrado"));
			
			if(status.getId().equals(comanda.getStatus().getId())) {
				throw new NegocioException("Comanda já está nesse status.");
			}
			
			if(comanda.getStatus().getId().equals(StatusModel.ABERTA) && newstatus.equals(StatusModel.PAGA)) {
				throw new NegocioException("Necessário fechar comanda antes de confirmar pagamento.");
			}
			
			if(newstatus.equals(StatusModel.AGUARDANDO_PAGAMENTO) ||
					newstatus.equals(StatusModel.PAGA)) {
				List<PedidoModel> pedidos = pedidoRepository.findPedidoByCpfAndComanda(comanda.getCliente().getLogin(), comanda.getId());
				if(pedidos.isEmpty()) {
					throw new NegocioException("Não é possível fechar Comanda sem pedidos feitos");
				}
				
				if(newstatus.equals(StatusModel.PAGA)) {
					List<PedidoModel> pedidosEmPreparacao = pedidoRepository.findPedidoByComandaAndStatus(comanda.getId(),Arrays.asList(StatusModel.EM_PREPARACAO));
					
					if(!pedidosEmPreparacao.isEmpty()) {
						throw new NegocioException("Será possível realizar o pagamento apenas após a entrega de todos os pedidos.");
					}
				}
			}
			
			comanda.setStatus(status);
			comanda.setTs_atualizacao(LocalDateTime.now());
			comandaRepository.save(comanda);
			
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean existsComandaByCpf(String cpf, List<Integer> statusId) {
		ComandaModel comandaModel = comandaRepository.findComandaByCpf(cpf, statusId);
		if(comandaModel == null) {
			return false;
		}
		return true;
	}
	
	public ComandaProjection findComandaByNumero(Integer numero){
		
		Optional<ComandaModel> comanda  = comandaRepository.findById(numero);
		if(!comanda.isPresent()) {
			throw new NegocioException("Número de comanda não encontrado");
		}
		
		ClienteProjection cliente = new ClienteProjection(comanda.get().getCliente().getNome(), comanda.get().getCliente().getTelefone(),
				comanda.get().getCliente().getEmail(), comanda.get().getCliente().getLogin(), comanda.get().getCliente().getPerfil().getId() ); 
		
		int mesa = comanda.get().getCliente().getMesa().getId();
		String garcom = comanda.get().getCliente().getMesa().getGarcom().getNome();
		
		List<PedidoModel> pedidos = pedidoRepository.findPedidoByCpfAndComanda(comanda.get().getCliente().getLogin(),comanda.get().getId());
		List<PedidosProjection> pedidos0 = new ArrayList<>();
		
		if(!pedidos.isEmpty()) {
			for(PedidoModel pedido : pedidos) {
				PedidosProjection p = new PedidosProjection();
				p.setIdItem(pedido.getItem().getId());
				p.setNomeItem(pedido.getItem().getNome());
				p.setPrecoItem(pedido.getItem().getPreco());
				p.setQuantidade(pedido.getQuantidade());
				p.setValor(pedido.getValor());
				p.setStatus(pedido.getStatus());
				p.setHorarioPedido(pedido.getHorarioPedido());
				p.setHorarioEntrega(pedido.getHorarioEntrega());
				p.setGarcom(garcom);
				p.setComanda(comanda.get().getId());
				
				pedidos0.add(p);
			}
		}
		ComandaProjection projection = new ComandaProjection(comanda.get().getId(),comanda.get().getValorTotal(), comanda.get().getStatus().getStatus(), 
				cliente, mesa, garcom ,pedidos0,formatarData(comanda.get().getDtAbertura()));
		
		return projection;
	}
	
	public List<ComandaProjection> findComandaByMesa(Integer mesa) throws Exception{
		try {
			Optional<MesaModel> mesa0 = mesaRepository.findById(mesa);
			
			if(!mesa0.isPresent()) {
				throw new NegocioException("Número de mesa não encontrado");
			}
			
			List<ComandaModel> comandas  = comandaRepository.findByMesa(mesa);
			List<ComandaProjection> comandasProjection = new ArrayList<ComandaProjection>();
			
			for(ComandaModel comanda : comandas) {
				ClienteProjection cliente = new ClienteProjection(comanda.getCliente().getNome(), comanda.getCliente().getTelefone(),
						comanda.getCliente().getEmail(), comanda.getCliente().getLogin(), comanda.getCliente().getPerfil().getId() ); 
				//int mesa = comanda.get().getCliente().getMesa().getId();
				String garcom = comanda.getCliente().getMesa().getGarcom().getNome();
				
				List<PedidoModel> pedidos = pedidoRepository.findPedidoByCpfAndComanda(comanda.getCliente().getLogin(), comanda.getId());
				List<PedidosProjection> pedidos0 = new ArrayList<>();
				
				if(!pedidos.isEmpty()) {
					for(PedidoModel pedido : pedidos) {
						PedidosProjection p = new PedidosProjection();
						p.setIdItem(pedido.getItem().getId());
						p.setNomeItem(pedido.getItem().getNome());
						p.setPrecoItem(pedido.getItem().getPreco());
						p.setQuantidade(pedido.getQuantidade());
						p.setValor(pedido.getValor());
						p.setStatus(pedido.getStatus());
						p.setHorarioPedido(pedido.getHorarioPedido());
						p.setHorarioEntrega(pedido.getHorarioEntrega());
						p.setGarcom(garcom);
						p.setComanda(comanda.getId());
						
						pedidos0.add(p);
					}
				}
				ComandaProjection projection = new ComandaProjection(comanda.getId(),comanda.getValorTotal(), comanda.getStatus().getStatus(),
						cliente, mesa,garcom ,pedidos0, formatarData(comanda.getDtAbertura()));
				comandasProjection.add(projection);
			}
			
			return comandasProjection;
			
		}catch (NegocioException e) {
			throw new NegocioException(e.getMessage());
		}
		catch (Exception e) {
			throw new Exception("Erro inesperado: "+e.getMessage());
		}
		
	}
	
	// método para retornar Lista de comandas do cliente logado
		public List<ComandaProjection> findMyComandas(){
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
		        
		        return this.findComandasByCpf(cpfDoUsuarioAutenticado); // retorna comanda o cliente logado

		    }
		    throw new NegocioException("Não foi possível localizar cliente");
		}
		
		public List<ComandaProjection> findComandasByCpf(String cpf){
			
			UserDetails clienteDetails  = clienteRepository.findByLogin(cpf);
			if(clienteDetails == null) {
				throw new NegocioException("Número de cpf não encontrado");
			}
			
			List<ComandaModel> comandas = comandaRepository.findComandasByCpf(cpf);
	        List<ComandaProjection> comandasP = new ArrayList<ComandaProjection>();
	        
	        for(ComandaModel c : comandas) {
	        	ComandaProjection cp = new ComandaProjection();
	        	
	        	ClienteProjection cliente = new ClienteProjection(c.getCliente().getNome(), c.getCliente().getTelefone(),
						c.getCliente().getEmail(), c.getCliente().getLogin(), c.getCliente().getPerfil().getId() ); 
				//int mesa = comanda.get().getCliente().getMesa().getId();
				String garcom = c.getCliente().getMesa().getGarcom().getNome();
				
				List<PedidoModel> pedidos = pedidoRepository.findPedidoByCpfAndComanda(c.getCliente().getLogin(), c.getId());
				List<PedidosProjection> pedidos0 = new ArrayList<>();
				
				if(!pedidos.isEmpty()) {
					for(PedidoModel pedido : pedidos) {
						PedidosProjection p = new PedidosProjection();
						p.setIdItem(pedido.getItem().getId());
						p.setNomeItem(pedido.getItem().getNome());
						p.setPrecoItem(pedido.getItem().getPreco());
						p.setQuantidade(pedido.getQuantidade());
						p.setValor(pedido.getValor());
						p.setStatus(pedido.getStatus());
						p.setHorarioPedido(pedido.getHorarioPedido());
						p.setHorarioEntrega(pedido.getHorarioEntrega());
						
						p.setGarcom(garcom);
						p.setComanda(c.getId());
						
						pedidos0.add(p);
					}
				}
	        	cp.setCliente(cliente);
	        	cp.setGarcom(garcom);
	        	cp.setId(c.getId());
	        	cp.setMesa(c.getCliente().getMesa().getId());
	        	cp.setPedidos(pedidos0);
	        	cp.setValorTotal(c.getValorTotal());
	        	cp.setStatus(c.getStatus().getStatus());
	        	cp.setDtAbertura(c.getDtAbertura() != null ? formatarData(c.getDtAbertura()): "-");
	        	
	        	comandasP.add(cp);
	        }
	        
	        
	        return comandasP; // retorna comanda o cliente logado
		}
		
		public String formatarData(LocalDateTime dateTime){
		     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		     return dateTime.format(formatter);
		}
}
