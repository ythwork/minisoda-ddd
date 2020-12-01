package com.ythwork.minisoda.common.web;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
public abstract class AbstractControllerTest {
	protected MockMvc mockMvc;
	
	abstract protected Object controller();
	
	@BeforeEach
	private void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller())
						.addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
						.alwaysDo(print())
						.build();
	}
	
	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(obj);
		log.info("JSON STRING FROM OBJECT : " + json);
		return json;
	}
	
	protected <T> T mapFromJson(String json, Class<T> claz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, claz);
	}
}
