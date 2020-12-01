package com.ythwork.minisoda.account.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.openapi.domain.Openapi;

import lombok.Getter;

@Getter
@Entity
@Table(name="account")
public class Account {
	@Id
	@Column(name="account_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="member_id")
	private Member member;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="openapi_id")
	private Openapi openapi;
	
	private Account() {}
	
	public Account(Member member, Openapi openapi) {
		this.member = member;
		this.openapi = openapi;
	}
}
