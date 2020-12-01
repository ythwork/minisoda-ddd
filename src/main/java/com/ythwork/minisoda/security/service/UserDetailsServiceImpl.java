package com.ythwork.minisoda.security.service;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.member.domain.MemberRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private MemberRepository memberRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = null;
		try {
			member = memberRepo.findByUsername(username);
		} catch(NoResultException e) {
			throw new UsernameNotFoundException("username에 맞는 멤버를 찾을 수 없습니다.");
		}
		return member;
	}

}
