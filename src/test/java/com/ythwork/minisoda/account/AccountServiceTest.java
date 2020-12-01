package com.ythwork.minisoda.account;

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

import com.ythwork.minisoda.account.dto.AccountAddInfo;
import com.ythwork.minisoda.account.dto.AccountInfo;
import com.ythwork.minisoda.account.service.AccountService;
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
import com.ythwork.minisoda.openapi.domain.OpenapiRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ContextConfiguration(classes=WebConfig.class)
public class AccountServiceTest {
	@Autowired
	private AccountService service;
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
		
		AccountAddInfo acntAddInfo = new AccountAddInfo(memberId, "A BANK", "123-45-6789");
		service.addAccount(acntAddInfo);
	}
	
	@Test
	public void addAccountTest() {
		Member member = memberRepo.findById(memberId);
		List<AccountInfo> acntInfoList = service.getAllAccountInfosByMember(member);
		
		AccountInfo acntInfo = acntInfoList.get(0);
		
		assertEquals(acntInfo.getAccountNumber(), "123-45-6789");
		assertEquals(acntInfo.getBalance(), 50000L);
		assertEquals(acntInfo.getOwner(), "양태환");
	}
	
	
}
