package com.ythwork.minisoda.member.web;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.member.dto.MemberAddInfo;
import com.ythwork.minisoda.member.dto.MemberInfo;
import com.ythwork.minisoda.member.hateoas.MemberModelAssembler;
import com.ythwork.minisoda.member.service.MemberService;
import com.ythwork.minisoda.security.exception.NotAllowedMemberException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
// Accept 헤더가 "application/json"인 요청만 받는다.
@RequestMapping(path="/member", produces="application/json")
@CrossOrigin(origins="*")
public class MemberController {
	private final MemberService memberService;
	private final MemberModelAssembler assembler;
	
	public MemberController(MemberService memberService, MemberModelAssembler assembler) {
		this.memberService = memberService;
		this.assembler = assembler;
	}
	
	// consumes : Content-type 이 application/json인 요청만 처리한다. 
	@PostMapping(path="/register", consumes="application/json")
	public ResponseEntity<?> registerMember(@RequestBody MemberAddInfo memberAddInfo, HttpServletRequest request) throws URISyntaxException {
		MemberInfo m = memberService.register(memberAddInfo);
		EntityModel<MemberInfo> entityModel = assembler.toModel(m);
		
		String loginUri = request.getRequestURL().toString().replace("member/register", "login");
		// location 헤더에 url을 채워 넣는다. 
		return ResponseEntity.created(new URI(loginUri)).body(entityModel);
	}
	
	@GetMapping("/{id}")
	public EntityModel<MemberInfo> getMember(@PathVariable("id") Long id, @AuthenticationPrincipal Member member) {
		MemberInfo memberInfo = memberService.getMemberInfoById(id);
		
		if(member.getId() != memberInfo.getMemberId()) {
			throw new NotAllowedMemberException("멤버 자신의 정보에만 접근이 가능합니다.");
		}
		
		return assembler.toModel(memberInfo);		
	}
}
