package com.ythwork.minisoda.openapi.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Getter
@Entity
@Table(name="bankcode")
@Access(AccessType.FIELD)
public class Bankcode {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="bankcode_id")
	private Long id;
	
	private String code;
	protected Bankcode() {}
	public Bankcode(String code) {
		this.code = code;
	}
}
