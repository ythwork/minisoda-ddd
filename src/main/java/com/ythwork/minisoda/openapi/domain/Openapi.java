package com.ythwork.minisoda.openapi.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ythwork.minisoda.openapi.domain.exception.NotEnoughBalanceException;

import lombok.Getter;

@Getter
@Entity
public class Openapi {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="openapi_id")
	private Long id;
	
	// 같은 애그리거트 내의 객체는 객체 참조로 가져온다.
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="bankcode_id")
	private Bankcode bankcode;
	
	@Column(name="account_number")
	private String accountNumber;
	
	private String owner;
	
	private Long balance;
	
	private void setBalance(Long balance) {
		this.balance = balance;
	}
	
	// 기능 추가
	public Long changeBalance(Long amount) {
		Long candBalance = balance + amount;
		if(candBalance < 0) {
			throw new NotEnoughBalanceException("잔액이 부족합니다.");
		}
		
		setBalance(candBalance);
		return candBalance;
	}
	
	public Long deposit(Long amount) {
		return changeBalance(amount);
	}
	
	public Long withdraw(Long amount) {
		return changeBalance(-amount);
	}
}
