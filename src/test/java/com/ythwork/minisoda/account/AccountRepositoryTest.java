package com.ythwork.minisoda.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.ythwork.minisoda.account.domain.Account;
import com.ythwork.minisoda.account.domain.AccountRepository;
import com.ythwork.minisoda.config.WebConfig;
import com.ythwork.minisoda.member.domain.Address;
import com.ythwork.minisoda.member.domain.Auth;
import com.ythwork.minisoda.member.domain.Email;
import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.member.domain.MemberRepository;
import com.ythwork.minisoda.member.domain.PhoneNumber;
import com.ythwork.minisoda.member.domain.Role;
import com.ythwork.minisoda.member.domain.RoleRepository;
import com.ythwork.minisoda.member.domain.RoleType;
import com.ythwork.minisoda.openapi.domain.Openapi;
import com.ythwork.minisoda.openapi.domain.OpenapiRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ContextConfiguration(classes=WebConfig.class)
public class AccountRepositoryTest {
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private MemberRepository memberRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private OpenapiRepository openapiRepo;
	
	private Long memberId;
	
	@BeforeEach
	public void init() {
		Set<Role> roles = new HashSet<>();
		Role role_user = roleRepo.findByRoleType(RoleType.ROLE_USER);
		Role role_admin = roleRepo.findByRoleType(RoleType.ROLE_ADMIN);
		roles.add(role_user);
		
		Member member = Member.build("태환", "양", 
				new Address("한국", "경기도", "군포시", "산본천로", "214"),
				new PhoneNumber("01066496270"),
				new Email("ythwork@naver.com"),
				new Auth("ythwork", "1234", roles));
		
		memberRepo.save(member);
		memberId = member.getId();
	}
	
	@Test
	public void duplicateAccountTest() {
		Member member= memberRepo.findById(memberId);
		Openapi openapi = openapiRepo.findById(1L);
		Account account = new Account(member, openapi);
		accountRepo.save(account);
		
		member= memberRepo.findById(memberId);
		openapi = openapiRepo.findById(1L);
		final Account acnt = new Account(member, openapi);
		
		assertThrows(PersistenceException.class, () -> {accountRepo.save(acnt);});
		
	}
	
	@Test
	public void getAccountsByMemberTest() {
		Member member= memberRepo.findById(memberId);
		Openapi openapi = openapiRepo.findById(1L);
		Account account = new Account(member, openapi);
		accountRepo.save(account);
		
		openapi = openapiRepo.findById(2L);
		account = new Account(member, openapi);
		accountRepo.save(account);
		
		openapi = openapiRepo.findById(3L);
		account = new Account(member, openapi);
		accountRepo.save(account);
		
		List<Account> accounts = accountRepo.findByMember(member);
		accounts.forEach(ac -> {
			log.info("account : [name : " + ac.getMember().getFullName() + 
					", account number : " + ac.getOpenapi().getAccountNumber() + 
					", balance : " + ac.getOpenapi().getBalance() + 
					"]");
		});
		
		assertEquals(accounts.size(), 3);
		
		
	}
}
