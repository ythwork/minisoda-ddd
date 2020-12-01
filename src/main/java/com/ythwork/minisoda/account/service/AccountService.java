package com.ythwork.minisoda.account.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ythwork.minisoda.account.domain.Account;
import com.ythwork.minisoda.account.domain.AccountRepository;
import com.ythwork.minisoda.account.domain.exception.AccountAlreadyExistsException;
import com.ythwork.minisoda.account.domain.exception.AccountNotFoundException;
import com.ythwork.minisoda.account.dto.AccountAddInfo;
import com.ythwork.minisoda.account.dto.AccountInfo;
import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.member.domain.MemberRepository;
import com.ythwork.minisoda.member.domain.exception.MemberNotFoundException;
import com.ythwork.minisoda.openapi.domain.Openapi;
import com.ythwork.minisoda.openapi.domain.OpenapiRepository;
import com.ythwork.minisoda.openapi.domain.exception.OpenapiNotFoundException;

@Service
@Transactional
public class AccountService {
	private AccountRepository accountRepo;
	private MemberRepository memberRepo;
	private OpenapiRepository openapiRepo;
	
	public AccountService(AccountRepository accountRepo, MemberRepository memberRepo, OpenapiRepository openapiRepo) {
		this.accountRepo = accountRepo;
		this.memberRepo = memberRepo;
		this.openapiRepo = openapiRepo;
	}
	
	public AccountInfo addAccount(AccountAddInfo accountAddInfo) {
		Member m = memberRepo.findById(accountAddInfo.getMemberId());
		if (m == null) throw new MemberNotFoundException();
		Openapi api = null;
		try {
			api = openapiRepo.findByBankcodeAndAccountNumber(accountAddInfo.getCode(), accountAddInfo.getAccountNumber());
		} catch(NoResultException e) {
			throw new OpenapiNotFoundException();
		}
		Account account = new Account(m, api);
		try {
			accountRepo.save(account);
		} catch(PersistenceException e) {
			throw new AccountAlreadyExistsException("이미 등록된 계좌입니다.");
		}
		
		return fromAccountToAccountInfo(account);
	}
	
	public AccountInfo getAccountInfoById(Long id) {
		Account account = accountRepo.findById(id);
		if(account == null) throw new AccountNotFoundException("계좌를 찾을 수 없습니다.");
		
		return fromAccountToAccountInfo(account);
	}
	
	public List<AccountInfo> getAllAccountInfosByMember(Member member) {
		List<Account> allAccountByMember = accountRepo.findByMember(member);
		return allAccountByMember.stream().map(this::fromAccountToAccountInfo).collect(Collectors.toList());
	}
	
	public AccountInfo fromAccountToAccountInfo(Account account) {
		return new AccountInfo(account.getOpenapi().getOwner(),
				account.getOpenapi().getBankcode().getCode(),
				account.getOpenapi().getAccountNumber(),
				account.getOpenapi().getBalance(),
				account.getId());
	}
}
