package com.ythwork.minisoda.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.security.jjwt.JwtManager;
import com.ythwork.minisoda.security.jjwt.exception.JwtAuthenticationException;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {
	private JwtManager jwtManager;
	private UserDetailsService userDetailsService;
	
	public AuthenticationTokenFilter(JwtManager jwtManager, UserDetailsService userDetailsService) {
		this.jwtManager = jwtManager;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// 나에게 보낸 게 맞나 확인하고.. 비밀 보장
			jwtManager.validateToken(request);
			// 인증 후 id 가져오기 
			String username = jwtManager.getUsername(request);
			// 사용자 정보 가져오기
			Member member = (Member)userDetailsService.loadUserByUsername(username);
			// Authentication Implementation
			// 인증 전 authentication 생성
//			public UsernamePasswordAuthenticationToken(Object principal, Object credentials) {
//				super(null);
//				this.principal = principal;
//				this.credentials = credentials;
//				setAuthenticated(false); 인증되지는 않았다.
//			}
			
			// 인증 후 authentication 생성
//			public UsernamePasswordAuthenticationToken(Object principal, Object credentials,
//					Collection<? extends GrantedAuthority> authorities) {
//				super(authorities);
//				this.principal = principal;
//				this.credentials = credentials; credential은 비밀번호인 경우가 큰데 인증 후에는 비밀번호를 제거한다.
//				super.setAuthenticated(true); 인증 되었다고 true
//			}
			
			
			// 인증된 상태의 authentication 생성 
			UsernamePasswordAuthenticationToken authentication = 
					// 두번째 인자인 credential은 비밀번호이다. 인증된 상태이므로 비밀번호는 보안상 null을 준다. 
					// getAuthorities()는 권한을 설정해준다. 
					new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
			// 요청에서 remoteAddress와 sessionId를 가져와 저장해둔다. 
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (JwtAuthenticationException e) {
			// web.ignoring()을 하면 이 필터를 아예 안 거치는 것이 아니라 
			// 에러가 나든 뭐가 되든 무시한다는 의미이다. 
			// 여기서 response.sendRedirect()를 하면 리다이렉트 된다.
		}
		
		filterChain.doFilter(request, response);
	}
	
}
