package com.ythwork.minisoda.transaction.domain;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ythwork.minisoda.member.domain.Member;

public interface TransactionRepository {
	void save(Transaction transaction);
	void remove(Transaction transaction);
	Transaction findById(Long id);
	List<Transaction> findByMember(Member member);
	List<Transaction> findAll(Specification<Transaction> specs);
	void deleteById(Long id);
}
