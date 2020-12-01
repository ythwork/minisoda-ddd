package com.ythwork.minisoda.transaction;

import static com.ythwork.minisoda.transaction.domain.TransactionSpec.amountGtOrEt;
import static com.ythwork.minisoda.transaction.domain.TransactionSpec.sendIs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

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
import com.ythwork.minisoda.transaction.domain.Transaction;
import com.ythwork.minisoda.transaction.domain.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ContextConfiguration(classes=WebConfig.class)
public class TransactionRepositoryTest {
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private MemberRepository memberRepo;
	@Autowired
	private OpenapiRepository openapiRepo;
	@Autowired
	private TransactionRepository transactionRepo;
	
	private Long memberId;
	
	private final Long sendId = 1L;
	private final Long recvIdOne = 4L;
	private final Long recvIdTwo = 5L;
	
	private Transaction transaction;
	
	private Member member;
	private Openapi send;
	private Openapi recv;
	
	private Long transactionIdOne;
	private Long transactionIdTwo;
	
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
	public void createTransaction() {
		member = memberRepo.findById(memberId);
		send = openapiRepo.findById(sendId);
		recv = openapiRepo.findById(recvIdOne);
		
		transaction = new Transaction(member, send, recv, 1000L);
		transactionRepo.save(transaction);
		transactionIdOne = transaction.getId();
		
		Transaction get = transactionRepo.findById(transactionIdOne);
		log.info("transaction: [member : " + get.getMember().getFullName() +
				", send : [account number : " + get.getSend().getAccountNumber() + ", balance : " + get.getSend().getBalance() + "]" + 
				", recv : [account number : " + get.getRecv().getAccountNumber() + ", balance : " + get.getRecv().getBalance() + "]" +
				", amount : " + get.getAmount() + 
				"]");
		
		assertEquals(get.getMember(), member);
	}
	
	private void createTransactions() {
		member = memberRepo.findById(memberId);
		send = openapiRepo.findById(sendId);
		recv = openapiRepo.findById(recvIdOne);
		
		Transaction transaction = new Transaction(member, send, recv, 1000L);
		transactionRepo.save(transaction);
		transactionIdOne = transaction.getId();
		
		recv = openapiRepo.findById(recvIdTwo);
		transaction = new Transaction(member, send, recv, 5000L);
		transactionRepo.save(transaction);
		transactionIdTwo = transaction.getId();
	}
	
	private void printTransaction(Transaction transaction) {
		log.info("transaction: [member : " + transaction.getMember().getFullName() +
				", send : [account number : " + transaction.getSend().getAccountNumber() + ", balance : " + transaction.getSend().getBalance() + "]" + 
				", recv : [account number : " + transaction.getRecv().getAccountNumber() + ", balance : " + transaction.getRecv().getBalance() + "]" +
				", amount : " + transaction.getAmount() + 
				"]");
	}
	
	@Test
	public void findAllTest() {
		createTransactions();
		
		Long amount = 1000L;
		Specification<Transaction> specs = where(sendIs(send)).and(amountGtOrEt(amount)); 
		
		List<Transaction> searchList = transactionRepo.findAll(specs);
		searchList.forEach(t -> { printTransaction(t);});
		
		assertEquals(searchList.size(), 2);
		
		amount = 4000L;
		specs = where(sendIs(send)).and(amountGtOrEt(amount)); 
		
		searchList = transactionRepo.findAll(specs);
		searchList.forEach(t -> { printTransaction(t);});
		
		assertEquals(searchList.size(), 1);
	}
	
	@Test
	public void findByMemberTest() {
		createTransactions();
		
		List<Transaction> transactions = transactionRepo.findByMember(member);
		transactions.forEach(t -> {printTransaction(t);});
		
		assertEquals(transactions.size(), 2);
	}
	
	@Test
	public void findByNoExistingId() {
		createTransactions();
		final Long noExistingId = transactionIdTwo + 100;
		Transaction t = transactionRepo.findById(noExistingId);
		log.info("findByNoExistingId : transaction --> " + t);
		
		assertEquals(t, null);
	}
}
