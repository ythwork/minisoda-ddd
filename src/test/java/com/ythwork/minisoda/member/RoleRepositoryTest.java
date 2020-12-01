package com.ythwork.minisoda.member;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.ythwork.minisoda.config.WebConfig;
import com.ythwork.minisoda.member.domain.Role;
import com.ythwork.minisoda.member.domain.RoleRepository;
import com.ythwork.minisoda.member.domain.RoleType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ContextConfiguration(classes=WebConfig.class)
public class RoleRepositoryTest {
	@Autowired
	private RoleRepository roleRepo;
	
	@Test
	public void findByRoleTypeTest() {
		RoleType roleType = RoleType.ROLE_USER;
		Role role = roleRepo.findByRoleType(roleType);
		
		log.info("role : [id : " + role.getId() + ", roleType : " + role.getRoleType().toString() + "]");
		
		assertEquals(role.getId(), 1L);
	}
}
