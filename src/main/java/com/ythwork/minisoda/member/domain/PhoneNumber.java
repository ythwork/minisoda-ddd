package com.ythwork.minisoda.member.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Embeddable
public class PhoneNumber {
	@Column(name="phone_number")
	private String number;
	
	private PhoneNumber() {}
	
	public PhoneNumber(String number) {
		this.number = number;
	}
	
	
}
