package com.ythwork.minisoda.openapi.domain;

import com.ythwork.minisoda.account.domain.Account;

public interface OpenapiRepository {
	Openapi findByAccount(Account account);
	Openapi findByBankcodeAndAccountNumber(String code, String accountNumber);
	Openapi findById(Long id);
}
