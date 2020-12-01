package com.ythwork.minisoda.member.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Address {
	private String country;
	private String province;
	private String city;
	private String street;
	@Column(name="house_number")
	private String houseNumber;
	
	public String fullAddress() {
		return country + " " + province + " " + city + " " + street + " " + houseNumber;
	}
}
