package com.ythwork.minisoda.transaction.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.openapi.domain.exception.NotEnoughBalanceException;
import com.ythwork.minisoda.security.exception.NotAllowedMemberException;
import com.ythwork.minisoda.transaction.domain.TransactionStatus;
import com.ythwork.minisoda.transaction.dto.TransactionAddInfo;
import com.ythwork.minisoda.transaction.dto.TransactionFilter;
import com.ythwork.minisoda.transaction.dto.TransactionInfo;
import com.ythwork.minisoda.transaction.hateoas.TransactionModelAssembler;
import com.ythwork.minisoda.transaction.service.TransactionService;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins="*")
public class TransactionController {
	private final TransactionService transactionService;
	private final TransactionModelAssembler assembler;
	
	public TransactionController(TransactionService transactionService, TransactionModelAssembler assembler) {
		this.transactionService = transactionService;
		this.assembler = assembler;
	}
	
	@GetMapping
	public CollectionModel<EntityModel<TransactionInfo>> search(@RequestBody TransactionFilter transactionFilter, @AuthenticationPrincipal Member member) {
		transactionFilter.setMemberId(member.getId());
		
		List<EntityModel<TransactionInfo>> transactions = transactionService.search(transactionFilter).stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(transactions, 
				linkTo(methodOn(TransactionController.class).search(transactionFilter, member)).withSelfRel());
	}
	
	@GetMapping("/{id}")
	public EntityModel<TransactionInfo> getTransaction(@PathVariable Long id, @AuthenticationPrincipal Member member) {
		TransactionInfo transactionInfo = transactionService.getTransactionInfoById(id);
		
		if(transactionInfo.getMemberId() != member.getId()) {
			throw new NotAllowedMemberException("회원님의 거래 내역만 조회할 수 있습니다.");
		}
		
		return assembler.toModel(transactionInfo);
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<TransactionInfo>> newTransaction(@RequestBody TransactionAddInfo transactionAddInfo, @AuthenticationPrincipal Member member) {
		TransactionInfo transactionInfo = transactionService.createTransaction(transactionAddInfo);
		
		return ResponseEntity.created(linkTo(methodOn(TransactionController.class).getTransaction(transactionInfo.getTransactionId(), member)).toUri())
					.body(assembler.toModel(transactionInfo));
	}
	
	@PutMapping("/{id}/complete")
	public ResponseEntity<?> complate(@PathVariable Long id, @AuthenticationPrincipal Member member) {
		TransactionInfo transactionInfo = transactionService.getTransactionInfoById(id);
		
		if(transactionInfo.getMemberId() != member.getId()) {
			throw new NotAllowedMemberException("회원의 계좌가 아니므로 송금할 수 없습니다.");
		}
		
		if(transactionInfo.getTransactionStatus() == TransactionStatus.IN_PROCESS) {
			try {
				TransactionInfo completed = transactionService.transfer(transactionInfo.getTransactionId());
				return ResponseEntity.ok(assembler.toModel(completed));
			} catch(NotEnoughBalanceException e) {
				TransactionInfo failed = transactionService.getTransactionInfoById(transactionInfo.getTransactionId());
				return ResponseEntity.badRequest().body(assembler.toModel(failed));
			}
		}
		
		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
				.body(Problem
						.create()
						.withTitle("Method not allowed")
						.withDetail("진행 중이 아닌경우 송금할 수 없습니다. 현재 트랜잭션 상태는 " + transactionInfo.getTransactionStatus() + "입니다."));
	}
	
	@DeleteMapping("/{id}/cancel")
	public ResponseEntity<?> cancel(@PathVariable Long id, @AuthenticationPrincipal Member member) {
		TransactionInfo transactionInfo = transactionService.getTransactionInfoById(id);
		
		if(transactionInfo.getMemberId() != member.getId()) {
			throw new NotAllowedMemberException("회원의 계좌가 아니므로 거래 내역을 취소할 수 없습니다.");
		}
		
		transactionInfo = transactionService.cancel(id);
		if(transactionInfo != null) {
			return ResponseEntity.ok(assembler.toModel(transactionInfo));
		}
		
		return ResponseEntity
					.status(HttpStatus.METHOD_NOT_ALLOWED)
					.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
					.body(Problem
								.create()
								.withTitle("Method not allowed")
								.withDetail("진행 중이 아닌 경우 취소할 수 없습니다."));	
	}
}
