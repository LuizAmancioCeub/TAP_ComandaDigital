package com.comandadigital.infra.security.roles;

public enum UserRoles {
	CLIENTE("ROLE_CLIENTE"),
    VISITANTE("ROLE_VISITANTE");
	
	private String role;
	
	UserRoles(String role){
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
	
	
}
