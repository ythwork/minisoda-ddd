package com.ythwork.minisoda.openapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

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
import com.ythwork.minisoda.openapi.domain.exception.NotEnoughBalanceException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ContextConfiguration(classes=WebConfig.class)
public class OPenapiRepositoryTest {
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private OpenapiRepository openapiRepo;
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private MemberRepository memberRepo;
	
	@Test
	public void findByCodeAndAccountNumberTest() {
		// given
		final String code = "A BANK";
		final String accountNumber = "123-45-6789";
		
		// when
		Openapi api = openapiRepo.findByBankcodeAndAccountNumber(code, accountNumber);
		log.info("api : [code : " + api.getBankcode().getCode() +
				", accountNumber : " + api.getAccountNumber() +
				", owner : " + api.getOwner() +
				", balance : " + api.getBalance() + 
				"]");
		
		// then
		assertEquals(api.getBankcode().getCode(), code);
		assertEquals(api.getAccountNumber(), accountNumber);
	}
	
	@Test
	public void NotEnoughBalanceExceptionTest() {
		final Long id = 1L; 
		Openapi api = openapiRepo.findById(id);
		assertThrows(NotEnoughBalanceException.class, () -> {api.changeBalance(-60000L);});
	}
	
	@Test
	public void findByAccountTest() {
		Set<Role> roles = new HashSet<>();
		Role role_user = roleRepo.findByRoleType(RoleType.ROLE_USER);
		roles.add(role_user);
		
		Member member = Member.build("태환", "양", 
				new Address("한국", "경기도", "군포시", "산본천로", "214"),
				new PhoneNumber("01066496270"),
				new Email("ythwork@naver.com"),
				new Auth("ythwork", "1234", roles));
		
		memberRepo.save(member);
		
		Member m= memberRepo.findById(member.getId());
		Openapi openapi = openapiRepo.findById(1L);
		Account account = new Account(m, openapi);
		accountRepo.save(account);
		Openapi api = openapiRepo.findByAccount(account);
		assertEquals(api, openapi);
	}
}
