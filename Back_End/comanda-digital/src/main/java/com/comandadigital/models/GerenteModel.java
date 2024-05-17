package com.comandadigital.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.comandadigital.infra.security.roles.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
@Table(name = "TB02_GERENTE")
@EqualsAndHashCode(of = "id")
public class GerenteModel extends RepresentationModel<GerenteModel> implements Serializable, UserDetails {
private static final long serialVersionUID = -3130915992537078226L;
	
	@jakarta.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id é auto increment e chave primaria
	@Column(name="NU_GERENTE", nullable = false, unique = true)
	@Setter(AccessLevel.NONE)
	private Integer id;
	
	@Column(name = "MATRICULA", nullable = false, unique = true)
	private String login;
	
	@Column(name = "CPF", nullable = false, unique = true)
	private String cpf;
	
	@Column(name = "NOME", nullable = false)
	 private String nome;
	
	@Column(name = "SENHA", nullable = false)
	private String senha;
	
	@Column(name = "TELEFONE", nullable = false, unique = true)
	private String telefone;
	
	@Column(name = "EMAIL", nullable = false, unique = true)
	private String email;
	
	@ManyToOne
	@JoinColumn(name = "NU_PERFIL", nullable = false)
	private PerfilModel perfil;
	
	public GerenteModel(String nome, String telefone,String email,String login, String cpf, String senha, PerfilModel perfil) {
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
		this.login = login;
		this.cpf = cpf;
		this.senha = senha;
		this.perfil = perfil;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		if("Gerente".equals(perfil.getPerfil())) {
			
			authorities.add(new SimpleGrantedAuthority(UserRole.GERENTE.getRole()));
		}
		
		return authorities;
	}
	
	// verificar quais roles esse usuario tem
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return login;
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
