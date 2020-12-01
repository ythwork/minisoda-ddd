package com.ythwork.minisoda.member;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ContextConfiguration(classes=WebConfig.class)
public class MemberRepositoryTest {
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private MemberRepository memberRepo;
	
	@Test
	public void createMember() {
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
		
		roles.clear();
		roles.add(role_user);
		member = Member.build("순신", "이", 
				new Address("한국", "경기도", "한양시", "육전대로", "111"),
				new PhoneNumber("01022223333"),
				new Email("sunsin@gmail.com"),
				new Auth("sunsin", "abcd", roles));
		memberRepo.save(member);
		
		Email email = new Email("ythwork@naver.com");
		member = memberRepo.findByEmail(email);
		log.info("member : [name : " + member.getFullName() +
				", address : " + member.getAddress().fullAddress() + 
				", email : " + member.getEmail().getAddress() + 
				"]");
		
		assertEquals(member.getEmail().getAddress(), "ythwork@naver.com");
		
	}
}
