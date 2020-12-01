package com.ythwork.minisoda.account;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ythwork.minisoda.account.dto.AccountAddInfo;
import com.ythwork.minisoda.account.web.AccountController;
import com.ythwork.minisoda.login.dto.LoginRequest;
import com.ythwork.minisoda.login.dto.LoginResponse;
import com.ythwork.minisoda.login.web.LoginController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
public class AccountControllerTest {
	private MockMvc mockMvc;
	@Autowired
	private AccountController accountController;
	@Autowired
	private LoginController loginController;
	
	private String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}
	
	private <T> T mapFromJson(String json, Class<T> claz) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, claz);
	}
	
	@BeforeEach
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(accountController, loginController)
						.addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
						.alwaysDo(print())
						.build();
	}
	
	@Test
	public void addAccountTest() throws JsonProcessingException, Exception {
		LoginRequest loginRequest = new LoginRequest("ythwork", "1234");
		AccountAddInfo accountAddInfo = new AccountAddInfo(109L,"A BANK", "123-45-6789");

		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapToJson(loginRequest))).andReturn();
		
		String responseEntity = result.getResponse().getContentAsString();
		EntityModel<?> entityModel = mapFromJson(responseEntity, EntityModel.class);
		LoginResponse loginResponse = (LoginResponse)entityModel.getContent();
		String token = loginResponse.getJwt();
		
		mockMvc.perform(MockMvcRequestBuilders.post("/account")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.content(mapToJson(accountAddInfo)))
				.andExpect(status().isCreated());
	}
}
