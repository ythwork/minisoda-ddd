package com.ythwork.minisoda.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.ythwork.minisoda.openapi.domain.exception.NotEnoughBalanceException;
import com.ythwork.minisoda.transaction.dto.TransactionAddInfo;
import com.ythwork.minisoda.transaction.dto.TransactionFilter;
import com.ythwork.minisoda.transaction.dto.TransactionInfo;
import com.ythwork.minisoda.transaction.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ContextConfiguration(classes=WebConfig.class)
public class TransactionServiceTest {
	@Autowired
	private TransactionService service;
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private MemberRepository memberRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private OpenapiRepository openapiRepo;
	
	private final Long amountForOne = 5000L;
	private final Long amountForTwo = 6000000L;
	private Long memberId;
	private Long accountId;
	private Long transactionIdForOne;
	private Long transactionIdForTwo;
	
	@BeforeEach
	public void init() {
		Set<Role> roles = new HashSet<>();
		Role role_user = roleRepo.findByRoleType(RoleType.ROLE_USER);
		roles.add(role_user);
		
		Member member = Member.build("태환", "양", 
				new Address("한국", "경기도", "군포시", "산본천로", "214"),
				new PhoneNumber("01066496270"),
				new Email("ythwork@naver.com"),
				new Auth("ythwork", "1234", roles));
		
		memberRepo.save(member);
		memberId = member.getId();
		
		Openapi openapi = openapiRepo.findById(1L);
		Account account = new Account(member, openapi);
		accountRepo.save(account);
		accountId = account.getId();
		
		TransactionAddInfo txAddInfo = new TransactionAddInfo(memberId, accountId, "B BANK", "222-33-4321", amountForOne);
		TransactionInfo transactionInfo = service.createTransaction(txAddInfo);
		transactionIdForOne = transactionInfo.getTransactionId();
		
		txAddInfo = new TransactionAddInfo(memberId, accountId, "B BANK", "222-33-4321", amountForTwo);
		transactionInfo = service.createTransaction(txAddInfo);
		transactionIdForTwo = transactionInfo.getTransactionId();
	}
	
	@Test
	public void createTransactionTest() {
		TransactionInfo txInfo = service.getTransactionInfoById(transactionIdForOne);
		
		assertEquals(txInfo.getMemberId(), memberId);
		assertEquals(txInfo.getSendAcntNum(), "123-45-6789");
		assertEquals(txInfo.getRecvCode(), "B BANK");
		assertEquals(txInfo.getRecvAcntNum(), "222-33-4321");
		assertEquals(txInfo.getAmount(), 5000L);
	}
	
	@Test 
	public void transferTestOne() {
		final Long sendId = 1L;
		final Long recvId = 5L;
		
		service.transfer(transactionIdForOne);
		
		Openapi send = openapiRepo.findById(sendId);
		Openapi recv = openapiRepo.findById(recvId);
		TransactionInfo transactionInfo = service.getTransactionInfoById(transactionIdForOne);
		
		assertEquals(send.getBalance(), 45000L);
		assertEquals(recv.getBalance(), 5500L);
		assertEquals(transactionInfo.getAfterBalance(), 45000L);
	}
	
	@Test
	public void transferTestTwo() {
		final Long sendId = 1L;
		final Long recvId = 5L;
		try {
			service.transfer(transactionIdForTwo);
		} catch(NotEnoughBalanceException e) {
			Openapi send = openapiRepo.findById(sendId);
			Openapi recv = openapiRepo.findById(recvId);
			TransactionInfo transactionInfo = service.getTransactionInfoById(transactionIdForTwo);
			assertEquals(send.getBalance(), 50000L);
			assertEquals(recv.getBalance(), 500L);
			assertEquals(transactionInfo.getAfterBalance(), 50000L - amountForTwo);
		}
	}
	
	@Test
	public void searchTest() {
		TransactionFilter transactionFilter = new TransactionFilter(memberId, accountId, null, null, null, null);
		List<TransactionInfo> list = service.search(transactionFilter);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).getMemberId(), memberId);
		assertEquals(list.get(1).getMemberId(), memberId);
		assertEquals(list.get(0).getSendCode(), "A BANK");
		
		transactionFilter = new TransactionFilter(memberId, null, null, null, null, 400L);
		list = service.search(transactionFilter);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).getAmount(), amountForOne);
		assertEquals(list.get(1).getAmount(), amountForTwo);
		
		transactionFilter = new TransactionFilter(memberId, null, null, null, null, 6000L);
		list = service.search(transactionFilter);
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getAmount(), amountForTwo);
		
	}
}
