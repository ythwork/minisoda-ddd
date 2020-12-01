package com.ythwork.minisoda.member.domain;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
@Entity
public class Member implements UserDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="member_id")
	private Long id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	public String getFullName() {
		return lastName + " " + firstName;
	}
	
	@Embedded
	private Address address;
	
	@Embedded
	private PhoneNumber phoneNumber;
	
	@Embedded
	private Email email;
	
	@Embedded
	private Auth auth;

	private Member() {}
	
	private void setId(Long id) {
		this.id = id;
	}
	
	private void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	private void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	private void setAddress(Address address) {
		this.address = address;
	}
	
	private void setPhoneNumber(PhoneNumber phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	private void setEmail(Email email) {
		this.email = email;
	}
	
	private void setAuth(Auth auth) {
		this.auth = auth;
	}
	
	public static Member build(String firstName, String lastName, Address address, PhoneNumber phoneNumber, Email email, Auth auth) {
		Member member = new Member();
		member.setFirstName(firstName);
		member.setLastName(lastName);
		member.setAddress(address);
		member.setPhoneNumber(phoneNumber);
		member.setEmail(email);
		member.setAuth(auth);
		return member;
	}
			
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return auth.getRoles().stream().map(
				role -> {
					return new SimpleGrantedAuthority(role.toString());
				}).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return auth.getPassword();
	}

	@Override
	public String getUsername() {
		return auth.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
