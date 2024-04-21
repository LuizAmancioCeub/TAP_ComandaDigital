package com.comandadigital.infra.security.roles;

public enum UserRole {
	CLIENTE("ROLE_CLIENTE"),
    VISITANTE("ROLE_VISITANTE"),
    BAR("ROLE_BAR"),
    COZINHA("ROLE_COZINHA"),
    CAIXA("ROLE_CAIXA"),
	GERENTE("ROLE_GERENTE"),
	GARCOM("ROLE_GARCOM");
	
	private String role;
	
	UserRole(String role){
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
	
	
	
}
