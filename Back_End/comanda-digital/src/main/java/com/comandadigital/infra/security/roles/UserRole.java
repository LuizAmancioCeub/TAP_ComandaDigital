package com.comandadigital.infra.security.roles;

public enum UserRole {
	CLIENTE("ROLE_CLIENTE"),
    VISITANTE("ROLE_VISITANTE"),
    COZINHA("ROLE_COZINHA");
	
	private String role;
	
	UserRole(String role){
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
	
	
	
}
