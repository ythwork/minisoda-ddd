package com.ythwork.minisoda.account.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ythwork.minisoda.account.dto.AccountAddInfo;
import com.ythwork.minisoda.account.dto.AccountInfo;
import com.ythwork.minisoda.account.hateoas.AccountModelAssembler;
import com.ythwork.minisoda.account.service.AccountService;
import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.security.exception.NotAllowedMemberException;

@RestController
@RequestMapping("/account")
public class AccountController {
	private final AccountService accountService;
	private final AccountModelAssembler assembler;
	
	public AccountController(AccountService accountService, AccountModelAssembler assembler) {
		this.accountService = accountService;
		this.assembler = assembler;
	}
	
	@GetMapping
	public CollectionModel<EntityModel<AccountInfo>> allAccounts(@AuthenticationPrincipal Member member) {
		List<AccountInfo> accounts = accountService.getAllAccountInfosByMember(member);
		List<EntityModel<AccountInfo>> entityModels = accounts.stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(entityModels, 
				linkTo(methodOn(AccountController.class).allAccounts(member)).withSelfRel());
	}
	
	@GetMapping("/{id}")
	public EntityModel<AccountInfo> getAccount(@PathVariable Long id) {
		AccountInfo accountInfo = accountService.getAccountInfoById(id);
		return assembler.toModel(accountInfo);
	}
	
	@PostMapping
	ResponseEntity<?> addAccount(@RequestBody AccountAddInfo accountAddInfo, @AuthenticationPrincipal Member member) {
		if(accountAddInfo.getMemberId() != member.getId()) {
			throw new NotAllowedMemberException("회원의 계좌 번호가 아닙니다.");
		}
		
		AccountInfo accountInfo = accountService.addAccount(accountAddInfo);
		EntityModel<AccountInfo> entityModel = assembler.toModel(accountInfo);
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
}
