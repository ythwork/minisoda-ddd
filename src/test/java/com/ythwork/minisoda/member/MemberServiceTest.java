package com.ythwork.minisoda.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.ythwork.minisoda.config.WebConfig;
import com.ythwork.minisoda.member.domain.Address;
import com.ythwork.minisoda.member.domain.Email;
import com.ythwork.minisoda.member.domain.PhoneNumber;
import com.ythwork.minisoda.member.domain.RoleRepository;
import com.ythwork.minisoda.member.domain.exception.MemberAlreadyExistsException;
import com.ythwork.minisoda.member.domain.exception.MemberNotFoundException;
import com.ythwork.minisoda.member.dto.MemberAddInfo;
import com.ythwork.minisoda.member.dto.MemberInfo;
import com.ythwork.minisoda.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ContextConfiguration(classes=WebConfig.class)
public class MemberServiceTest {
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private MemberService service;
	
	private Set<String> roles;
	private MemberInfo info;
	
	@BeforeEach
	public void createMember() {
		roles = new HashSet<>();
		roles.add("ADMIN");
		roles.add("USER");
		MemberAddInfo memberAddInfo = new MemberAddInfo("태환", "양", 
				new Address("한국", "경기도", "군포시", "산본천로", "214"),
				new PhoneNumber("01066496270"),
				new Email("ythwork@naver.com"),
				"ythwork", "1234", roles);
		info = service.register(memberAddInfo);
	}
	
	@Test
	public void registerTestOne() {
		MemberInfo m = service.getMemberInfoById(info.getMemberId());
		
		assertEquals(m.getFullName(), "양 태환");
		assertEquals(m.getEmail(), new Email("ythwork@naver.com"));
		assertEquals(m.getMemberId(), info.getMemberId());
		assertEquals(m.getPhoneNumber(), new PhoneNumber("01066496270"));
		assertEquals(m.getAddress(), new Address("한국", "경기도", "군포시", "산본천로", "214"));
	}
	
	@Test
	public void registerTestTwo() {
		final MemberAddInfo memberAddInfo = new MemberAddInfo("영실", "장",
				new Address("한국", "경기도", "한양시", "육전대로", "111"),
				new PhoneNumber("01022223333"),
				new Email("ythwork@naver.com"),
				"youngsil", "1234", roles);
		
		assertThrows(MemberAlreadyExistsException.class, () -> {service.register(memberAddInfo);});
	}
	
	@Test 
	public void getMemberInfoByUsernameTest() {
		MemberInfo memberInfo = service.getMemberInfoByUsername("ythwork");
		
		assertEquals(memberInfo.getEmail(), new Email("ythwork@naver.com"));
		assertEquals(memberInfo.getPhoneNumber(), new PhoneNumber("01066496270"));
		
		assertThrows(MemberNotFoundException.class, () -> {service.getMemberInfoByUsername("yourfault");});
		
	}
	
	
}
