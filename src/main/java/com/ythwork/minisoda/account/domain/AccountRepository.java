package com.ythwork.minisoda.account.domain;

import java.util.List;

import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.openapi.domain.Openapi;

public interface AccountRepository {
	void save(Account account);
	Account findById(Long id);
	void remove(Account account);
	List<Account> findByMember(Member member);
	Account findByOpenapi(Openapi openapi);
}
