package com.ythwork.minisoda.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.ythwork.minisoda.login.dto.LoginRequest;

@Service
public class LoginService {
	@Autowired
	private AuthenticationManager authenticationManager;
	public Authentication authenticate(LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		try {
			// 인증되지 않은 authentication 객체를 생성해서 인자로 전달하면
			// 인증되었을 경우 인증된 authentication 객체를 반환한다.
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			return authentication;
		} catch(AuthenticationException e) {
			throw e;
		}
	}
}
