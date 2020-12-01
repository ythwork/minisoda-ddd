package com.ythwork.minisoda.member.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ythwork.minisoda.security.credential.PasswordManager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Auth {
	private String username;
	private String password;
	
	// authorities similar to roles
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="member_role",
	joinColumns=@JoinColumn(name="member_id"),
	inverseJoinColumns=@JoinColumn(name="role_id"))
	private Set<Role> roles = new HashSet<>();
	
	private Auth() {}
	
	public Auth(String username, String password, Set<Role> roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
	
	private void setUsername(String username) {
		this.username = username;
	}
	
	private void setPassword(String rawPassword) {
		PasswordEncoder passwordEncoder = PasswordManager.getPasswordEncoder();
		String password = passwordEncoder.encode(rawPassword);
		this.password = password;
	}
	
	private void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public static Auth build(String username, String rawPassword, Set<Role> roles) {
		Auth auth = new Auth();
		auth.setUsername(username);
		auth.setPassword(rawPassword);
		auth.setRoles(roles);
		return auth;
	}
	
	// functions
}
