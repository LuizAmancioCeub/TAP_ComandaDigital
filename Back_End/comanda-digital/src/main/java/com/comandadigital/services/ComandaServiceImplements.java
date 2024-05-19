package com.comandadigital.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		
		ClienteProjection cliente = new ClienteProjection(comandaModel.getCliente().getNome(), comandaModel.getCliente().getTelefone(),
				comandaModel.getCliente().getEmail(), comandaModel.getCliente().getLogin(), comandaModel.getCliente().getPerfil().getId() ); 
		
		int mesa = comandaModel.getCliente().getMesa().getId();
		String garcom = comandaModel.getCliente().getMesa().getGarcom().getNome();
		
		List<PedidoModel> pedidos = pedidoRepository.findPedidoByCpf(cpf);
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
				pedidos0.add(p);
			}
		}
		ComandaProjection projection = new ComandaProjection(comandaModel.getId(),comandaModel.getValorTotal(),
				comandaModel.getStatus().getStatus(), cliente, mesa, garcom,pedidos0);
		
		return projection;
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
	        
	        ComandaProjection comanda = findComandaByCpf(cpfDoUsuarioAutenticado,Arrays.asList(StatusModel.ABERTA, StatusModel.AGUARDANDO_PAGAMENTO));
	        if(comanda == null) {
	        	throw new RuntimeException("Comanda não encontrada para cpf "+cpfDoUsuarioAutenticado);
	        }
	        // hipermidia para buscar os pedidos
	       // comandaCliente.add(linkTo(methodOn(PedidoController.class).getPedidosByCpf(cpfDoUsuarioAutenticado)).withRel("pedidos"));
	        
	        return comanda; // retorna comanda o cliente logado

	    }
	    return null;
	}
	
	public void updateValor(Integer id,double valor) {
		ComandaModel comanda = comandaRepository.findById(id).orElseThrow(() -> new RuntimeException("Comanda não encontrado com ID: " + id));
		
		var valorTotal = comanda.getValorTotal() + valor;
		BigDecimal v = BigDecimal.valueOf(valorTotal).setScale(2, RoundingMode.HALF_EVEN);
		valorTotal = v.doubleValue();
		
		comanda.setValorTotal(valorTotal);
		comandaRepository.save(comanda);
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
		
		List<PedidoModel> pedidos = pedidoRepository.findPedidoByCpf(comanda.get().getCliente().getLogin());
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
				pedidos0.add(p);
			}
		}
		ComandaProjection projection = new ComandaProjection(comanda.get().getId(),comanda.get().getValorTotal(), comanda.get().getStatus().getStatus(), 
				cliente, mesa, garcom ,pedidos0);
		
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
				
				List<PedidoModel> pedidos = pedidoRepository.findPedidoByCpf(comanda.getCliente().getLogin());
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
						pedidos0.add(p);
					}
				}
				ComandaProjection projection = new ComandaProjection(comanda.getId(),comanda.getValorTotal(), comanda.getStatus().getStatus(),
						cliente, mesa,garcom ,pedidos0);
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

}
