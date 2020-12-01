package com.ythwork.minisoda.member.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Embeddable
@EqualsAndHashCode
public class Email {
	@Column(name="email")
	private String address;
	
	private Email() {}
	
	public Email(String address) {
		this.address = address;
	}
}
