package com.comandadigital.models;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB06_CLIENTE")
@EqualsAndHashCode(of = "id")
public class ClienteModel extends RepresentationModel<GarcomModel> implements Serializable, UserDetails {
	private static final long serialVersionUID = 8016917473065647291L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id é auto increment e chave primaria
	@Column(name="NU_CLIENTE", nullable = false, unique = true)
	@Setter(AccessLevel.NONE)
	private Integer id;
	
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
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
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
