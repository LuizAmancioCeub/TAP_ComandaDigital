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
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB04_COZINHA")
public class CozinhaModel extends RepresentationModel<GarcomModel> implements Serializable, UserDetails {
	private static final long serialVersionUID = 5818006224569466399L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id é auto increment e chave primaria
	@Column(name = "NU_COZINHA", nullable = false, unique = true)
	@Setter(AccessLevel.NONE) // para não modificar o id
	private Integer id;
	
	@Column(name = "TIPO", nullable = false)
	private String tipo;
	
	@Column(name = "SENHA", nullable = false)
	private String senha;
	
	@ManyToOne
	@JoinColumn(name = "NU_PERFIL", nullable = false)
	private PerfilModel perfil;
	
	public CozinhaModel(String tipo, String senha, PerfilModel perfil) {
		this.tipo = tipo;
		this.senha = senha;
		this.perfil = perfil;
	}
	
	// definindo tipo de usuário
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		if("COZINHA".equals(perfil.getPerfil())) {
			authorities.add(new SimpleGrantedAuthority(UserRole.COZINHA.getRole()));
		}else if("BAR".equals(perfil.getPerfil())) {
			authorities.add(new SimpleGrantedAuthority(UserRole.BAR.getRole()));
		}
		
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return tipo;
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
