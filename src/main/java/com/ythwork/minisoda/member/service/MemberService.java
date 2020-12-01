package com.ythwork.minisoda.member.service;

import java.util.Set;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ythwork.minisoda.member.domain.Auth;
import com.ythwork.minisoda.member.domain.Email;
import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.member.domain.MemberRepository;
import com.ythwork.minisoda.member.domain.Role;
import com.ythwork.minisoda.member.domain.RoleRepository;
import com.ythwork.minisoda.member.domain.exception.MemberAlreadyExistsException;
import com.ythwork.minisoda.member.domain.exception.MemberNotFoundException;
import com.ythwork.minisoda.member.dto.MemberAddInfo;
import com.ythwork.minisoda.member.dto.MemberInfo;

@Service
@Transactional
public class MemberService {
	private MemberRepository memberRepo;
	private RoleRepository roleRepo;
	
	public MemberService(MemberRepository memberRepo, RoleRepository roleRepo) {
		this.memberRepo = memberRepo;
		this.roleRepo = roleRepo;
	}
	
	public MemberInfo register(MemberAddInfo memberAddInfo) {
		Set<Role> roles = roleRepo.convertFromString(memberAddInfo.getRoles());
		Auth auth = Auth.build(memberAddInfo.getUsername(), memberAddInfo.getPassword(), roles);
		
		Member member = Member.build(memberAddInfo.getFirstName(), 
				memberAddInfo.getLastName(), 
				memberAddInfo.getAddress(), 
				memberAddInfo.getPhoneNumber(), 
				memberAddInfo.getEmail(), 
				auth);
		if(memberRepo.exists(member)) {
			throw new MemberAlreadyExistsException("이메일 " + member.getEmail().getAddress() + "을 가진 회원이 이미 존재합니다.");
		}
		memberRepo.save(member);
		return fromMemberToMemberInfo(member);
	}
	
	public MemberInfo getMemberInfoById(Long id) {
		Member member = memberRepo.findById(id);
		if(member == null) {
			throw new MemberNotFoundException("멤버를 찾을 수 없습니다.");
		}
		return fromMemberToMemberInfo(member);
	}
	
	public MemberInfo getMemberInfoByUsername(String username) {
		Member member = null;
		try {
			member = memberRepo.findByUsername(username);
		} catch(NoResultException e) {
			throw new MemberNotFoundException("멤버를 찾을 수 없습니다.");
		}
		return fromMemberToMemberInfo(member);
	}
	
	public MemberInfo getMemberInfoByEmail(Email email) {
		Member member = null;
		try {
			member = memberRepo.findByEmail(email);
		} catch(NoResultException e) {
			throw new MemberNotFoundException("멤버를 찾을 수 없습니다.");
		}
		return fromMemberToMemberInfo(member);
	}
	
	public MemberInfo fromMemberToMemberInfo(Member member) {
		return new MemberInfo(
				member.getFullName(),
				member.getAddress(),
				member.getPhoneNumber(),
				member.getEmail(),
				member.getId());
	}
}
