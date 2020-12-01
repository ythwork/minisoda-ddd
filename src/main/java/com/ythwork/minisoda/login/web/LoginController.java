package com.ythwork.minisoda.login.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ythwork.minisoda.login.dto.LoginRequest;
import com.ythwork.minisoda.login.dto.LoginResponse;
import com.ythwork.minisoda.login.hateoas.LoginModelAssembler;
import com.ythwork.minisoda.login.service.LoginService;
import com.ythwork.minisoda.member.dto.MemberInfo;
import com.ythwork.minisoda.member.service.MemberService;
import com.ythwork.minisoda.security.jjwt.JwtManager;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins="*")
public class LoginController {
	private JwtManager jwtManager;
	private LoginService loginService;
	private MemberService memberService;
	private LoginModelAssembler assembler;
	public LoginController(JwtManager jwtManager, LoginService loginService, MemberService memberService, LoginModelAssembler assembler) {
		this.jwtManager = jwtManager;
		this.loginService = loginService;
		this.memberService = memberService;
		this.assembler = assembler;
	}
	
	@PostMapping
	public EntityModel<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		Authentication authentication = null;
		try {
			authentication = loginService.authenticate(loginRequest);
		} catch(AuthenticationException e) {
			return EntityModel.of(new LoginResponse(),
					linkTo(methodOn(LoginController.class).login(loginRequest)).withSelfRel());
		}
		String token = jwtManager.issueToken(authentication);
		MemberInfo memberInfo = memberService.getMemberInfoByUsername(loginRequest.getUsername());
		LoginResponse loginResponse = new LoginResponse(token, memberInfo);
		
		return assembler.toModel(loginResponse);
	}
}
