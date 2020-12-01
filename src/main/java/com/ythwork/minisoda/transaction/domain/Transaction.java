package com.ythwork.minisoda.transaction.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.openapi.domain.Openapi;

import lombok.Getter;

@Getter
@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="transaction_id")
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="member_id")
	private Member member;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="send_account")
	private Openapi send;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="recv_account")
	private Openapi recv;
	
	private Long amount;
	
	@Column(name="after_balance")
	private Long afterBalance = 0L;
	
	@Enumerated(EnumType.STRING)
	@Column(name="transaction_status")
	private TransactionStatus transactionStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date processAt;
	
	private Transaction() {}
	
	public Transaction(Member member, Openapi send, Openapi recv, Long amount) {
		this.member = member;
		this.send = send;
		this.recv = recv;
		this.amount = amount;
	}
	
	private void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	
	private void setAfterBalance(Long afterBalance) {
		this.afterBalance = afterBalance;
	}
	
	public void changeTransactionStatus(TransactionStatus transactionStatus) {
		setTransactionStatus(transactionStatus);
	}
	
	public void changeAfterBalance(Long afterBalance) {
		setAfterBalance(afterBalance);
	}
	
	public boolean cancel() {
		if(transactionStatus == TransactionStatus.IN_PROCESS) {
			changeTransactionStatus(TransactionStatus.CANCELED);
			return true;
		} 
		return false;
	}
	
	@PrePersist
	void startProcessing() {
		changeTransactionStatus(TransactionStatus.IN_PROCESS);
		processAt = new Date();
	}

}
