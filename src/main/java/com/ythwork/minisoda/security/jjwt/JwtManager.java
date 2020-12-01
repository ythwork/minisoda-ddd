package com.ythwork.minisoda.security.jjwt;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.security.jjwt.exception.JwtAuthenticationException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtManager {
	// Private Key
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.expiration}")
	private Long expiration;
	// encode token
	public String issueToken(Authentication authentication) {
		Member member = (Member)authentication.getPrincipal();
		return Jwts.builder()
					.setSubject(member.getAuth().getUsername())
					.setIssuedAt(new Date())
					.setExpiration(new Date(new Date().getTime() + expiration))
					// 프라이빗 키로 전자 서명 : 내가 보낸 것이 확실하다.
					.signWith(SignatureAlgorithm.HS512	, secretKey)
					.compact();
	}
	
	// 인증 후 ID
	private String getUsername(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public String getUsername(HttpServletRequest request) {
		String jwt = getJwtFromRequestHeader(request);
		if(jwt == null) {
			throw new JwtAuthenticationException("요청 헤더에 JWT가 없습니다.");
		}
		
		return getUsername(jwt);
	}
	
	
	private void validateToken(String token) {
		if(token == null) {
			throw new JwtAuthenticationException("요청 헤더에 JWT가 없습니다.");
		}
		
		try {
			// Private Key로 decode 할 수 있다면 
			// 나에게 보낸 게 확실하다. 비밀 보장!!
			// parseClaimsJws : decode
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		} catch(IllegalArgumentException e) {
			throw new JwtAuthenticationException("JWT 클레임에 페어가 없습니다.");
		} catch(ExpiredJwtException e) {
			throw new JwtAuthenticationException("토큰이 만료되었습니다.");
		} catch(UnsupportedJwtException e) {
			throw new JwtAuthenticationException("지원하지 않는 JWT입니다.");
		} catch(SignatureException e) {
			throw new JwtAuthenticationException("JWT 서명이 잘못되었습니다.");
		} catch(RuntimeException e) {
			throw new JwtAuthenticationException("알 수 없는 에러가 발생했습니다.");
		}
	}
	
	public void validateToken(HttpServletRequest request) {
		String jwt = getJwtFromRequestHeader(request);
		validateToken(jwt);
	}
	
	public String getJwtFromRequestHeader(HttpServletRequest request) {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if(tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
		}
		return token;
	}
}
