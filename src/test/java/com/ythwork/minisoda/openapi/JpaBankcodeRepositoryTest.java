package com.ythwork.minisoda.openapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.ythwork.minisoda.config.WebConfig;
import com.ythwork.minisoda.openapi.domain.Bankcode;
import com.ythwork.minisoda.openapi.domain.BankcodeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ContextConfiguration(classes=WebConfig.class)
public class JpaBankcodeRepositoryTest {
	@Autowired
	private BankcodeRepository bankRepo;
	
	@Test
	public void findByCodeTest() {
		// given
		final String code = "A BANK";
		
		// when
		Bankcode bankcode = bankRepo.findByCode(code);
		log.info("bankcode : [id = " + bankcode.getId() + ", code = " + bankcode.getCode() + "]");
		
		// then
		assertEquals(bankcode.getCode(), code);
	}
	
}
