package com.ythwork.minisoda.member;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ythwork.minisoda.common.web.AbstractControllerTest;
import com.ythwork.minisoda.member.domain.Address;
import com.ythwork.minisoda.member.domain.Email;
import com.ythwork.minisoda.member.domain.PhoneNumber;
import com.ythwork.minisoda.member.dto.MemberAddInfo;
import com.ythwork.minisoda.member.hateoas.MemberModelAssembler;
import com.ythwork.minisoda.member.web.MemberController;


public class MemberControllerTest extends AbstractControllerTest {
	@Autowired
	private MemberController memberController;
	private MemberModelAssembler assembler = new MemberModelAssembler();
	
	@Override
	protected Object controller() {
		return memberController;
	}
	 
	@Test
	public void registerMemberTest() throws JsonProcessingException, Exception {
		Set<String> roles = new HashSet<>();
		roles.add("ADMIN");
		roles.add("USER");
		MemberAddInfo memberAddInfo = new MemberAddInfo("태환", "양", 
				new Address("한국", "경기도", "군포시", "산본천로", "214"),
				new PhoneNumber("01066496270"),
				new Email("ythwork@naver.com"),
				"ythwork", "1234", roles);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/member/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapToJson(memberAddInfo)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/login"));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/member/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapToJson(memberAddInfo)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/login"));
	}
}
