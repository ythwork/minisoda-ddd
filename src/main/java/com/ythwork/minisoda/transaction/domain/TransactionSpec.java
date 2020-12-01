package com.ythwork.minisoda.transaction.domain;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.openapi.domain.Openapi;

public class TransactionSpec {
	public static Specification<Transaction> memberIs(Member member) {
		return (transaction, cq, cb) -> {
			if(member == null) return null;
			
			return cb.equal(transaction.get("member"), member);
		};
	}
	
	public static Specification<Transaction> sendIs(Openapi send) {
		return (transaction, cq, cb) -> {
			if(send == null) return null;
			
			return cb.equal(transaction.get("send"), send);
		};
	}
	
	public static Specification<Transaction> processAtBetween(Date from, Date to) {
		return (transaction, cq, cb) -> {
			if(from == null && to == null) {
				return null;
			} else if(from == null) {
				return cb.lessThanOrEqualTo(transaction.get("processAt"), to);
			} else if(to == null) {
				return cb.greaterThanOrEqualTo(transaction.get("processAt"), from);
			} else {
				return cb.between(transaction.get("processAt"), from, to);
			}
		};
	}
	
	public static Specification<Transaction> statusIs(TransactionStatus status) {
		return (transaction, cq, cb) -> {
			if(status == null) return null;
			
			return cb.equal(transaction.get("transactionStatus"), status);
		};
	}
	
	public static Specification<Transaction> amountGtOrEt(Long amount) {
		return (transaction, cq, cb) -> {
			if(amount == null) return null;
			
			return cb.greaterThanOrEqualTo(transaction.get("amount"), amount);
		};
	}
}
