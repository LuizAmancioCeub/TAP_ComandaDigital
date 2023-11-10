package com.comandadigital.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.comandadigital.infra.security.roles.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB06_CLIENTE")
@EqualsAndHashCode(of = "id")
public class ClienteModel extends RepresentationModel<GarcomModel> implements Serializable, UserDetails {
	private static final long serialVersionUID = 8016917473065647291L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID) // id é auto increment e chave primaria
	@Column(name="NU_CLIENTE", nullable = false, unique = true)
	private UUID id;
	
	@Column(name = "CPF", nullable = false, unique = true)
	private String cpf;
	
	@Column(name = "NOME", nullable = false)
	 private String nome;
	
	@Column(name = "SENHA", nullable = false)
	private String senha;
	
	@Column(name = "TELEFONE", nullable = false, unique = true)
	private String telefone;
	
	@ManyToOne
	@JoinColumn(name = "NU_PERFIL", nullable = false)
	private PerfilModel perfil;

	@ManyToOne
	@JoinColumn(name = "NU_MESA", nullable = false)
	private MesaModel mesa;
	
	
	
	public ClienteModel(String cpf, String nome,String senha,String telefone, PerfilModel perfil) {
		this.cpf = cpf;
		this.senha = senha;
		this.perfil = perfil;
		this.nome = nome;
		this.telefone = telefone;
	}
	
	// verificar quais roles esse usuario tem
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		if("Cliente".equals(perfil.getPerfil())) {
			
			authorities.add(new SimpleGrantedAuthority(UserRole.CLIENTE.getRole()));
		}
		else if("Visitante".equals(perfil.getPerfil())) {
			
			authorities.add(new SimpleGrantedAuthority(UserRole.VISITANTE.getRole()));
		} 
		
		return authorities;
	}

	@Override
	public String getPassword() {
		
		return senha;
	}

	@Override
	public String getUsername() {
		
		return cpf;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
