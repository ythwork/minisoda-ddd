package com.ythwork.minisoda.transaction.service;

import static com.ythwork.minisoda.transaction.domain.TransactionSpec.amountGtOrEt;
import static com.ythwork.minisoda.transaction.domain.TransactionSpec.memberIs;
import static com.ythwork.minisoda.transaction.domain.TransactionSpec.processAtBetween;
import static com.ythwork.minisoda.transaction.domain.TransactionSpec.sendIs;
import static com.ythwork.minisoda.transaction.domain.TransactionSpec.statusIs;
import static org.springframework.data.jpa.domain.Specification.where;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ythwork.minisoda.account.domain.AccountRepository;
import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.member.domain.MemberRepository;
import com.ythwork.minisoda.openapi.domain.Openapi;
import com.ythwork.minisoda.openapi.domain.OpenapiRepository;
import com.ythwork.minisoda.openapi.domain.exception.NotEnoughBalanceException;
import com.ythwork.minisoda.openapi.domain.exception.OpenapiNotFoundException;
import com.ythwork.minisoda.transaction.domain.Transaction;
import com.ythwork.minisoda.transaction.domain.TransactionRepository;
import com.ythwork.minisoda.transaction.domain.TransactionStatus;
import com.ythwork.minisoda.transaction.domain.exception.TransactionNotFoundException;
import com.ythwork.minisoda.transaction.dto.TransactionAddInfo;
import com.ythwork.minisoda.transaction.dto.TransactionFilter;
import com.ythwork.minisoda.transaction.dto.TransactionInfo;

@Service
@Transactional(noRollbackFor=NotEnoughBalanceException.class)
public class TransactionService {
	private TransactionRepository transactionRepo;
	private MemberRepository memberRepo;
	private AccountRepository accountRepo;
	private OpenapiRepository openapiRepo;
	
	public TransactionService(TransactionRepository transactionRepo, MemberRepository memberRepo,
								AccountRepository accountRepo, OpenapiRepository openapiRepo) {
		this.transactionRepo = transactionRepo;
		this.memberRepo = memberRepo;
		this.accountRepo = accountRepo;
		this.openapiRepo = openapiRepo;
	}
	
	public TransactionInfo createTransaction(TransactionAddInfo transactionAddInfo) {
		Openapi send = null;
		Openapi recv = null;
		try {
			send = openapiRepo.findByAccount(accountRepo.findById(transactionAddInfo.getSendAcntId()));
			recv = openapiRepo.findByBankcodeAndAccountNumber(transactionAddInfo.getRecvCode(), transactionAddInfo.getRecvAcntNum());
		} catch(NoResultException e) {
			throw new OpenapiNotFoundException("계좌 내역을 찾을 수 없습니다.");
		}
		Transaction transaction = new Transaction(memberRepo.findById(transactionAddInfo.getMemberId()),
								send, recv, transactionAddInfo.getAmount());
		transactionRepo.save(transaction);
		
		return fromTransactionToTransactionInfo(transaction);
	}
	
	public TransactionInfo getTransactionInfoById(Long id) {
		Transaction transaction = transactionRepo.findById(id);
		if(transaction == null) throw new TransactionNotFoundException("거래 내역을 찾을 수 없습니다.");
		return fromTransactionToTransactionInfo(transaction);
	}
	
	public TransactionInfo transfer(Long transactionId) {
		Transaction transaction = transactionRepo.findById(transactionId);
		if(transaction == null) throw new TransactionNotFoundException("거래 내역을 찾을 수 없습니다.");
		
		Openapi send = transaction.getSend();
		Openapi recv = transaction.getRecv();
		Long amount = transaction.getAmount();
		
		try {
			send.withdraw(amount);
			recv.deposit(amount);
		} catch(NotEnoughBalanceException e) {
			transaction.changeTransactionStatus(TransactionStatus.FAILED);
			transaction.changeAfterBalance(send.getBalance() - amount);
			throw e;
		}
		
		transaction.changeTransactionStatus(TransactionStatus.SUCCEEDED);
		transaction.changeAfterBalance(send.getBalance());
		
		return fromTransactionToTransactionInfo(transaction);
	}
	
	private Specification<Transaction> parseTransactionFilter(TransactionFilter filter) {
		// 로그인 상태이므로 반드시 존재
		Member member = memberRepo.findById(filter.getMemberId());
		Openapi send = null;
		if(filter.getSendAcntId() != null)
			send = openapiRepo.findByAccount(accountRepo.findById(filter.getSendAcntId()));
		
		TransactionStatus status = null;
		if(filter.getStatus() != null)
			status = filter.getStatus();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date from = null;
		Date to = null;
		try {
			if(filter.getFrom() != null)
				from = format.parse(filter.getFrom());
			if(filter.getTo() != null)
				to = format.parse(filter.getTo());
		} catch(ParseException e) {}
		Long amount = filter.getAmount();
		
		return where(memberIs(member))
					.and(sendIs(send))
					.and(processAtBetween(from, to))
					.and(statusIs(status))
					.and(amountGtOrEt(amount));
	}
	
	public List<TransactionInfo> search(TransactionFilter filter) {
		Specification<Transaction> combined = parseTransactionFilter(filter);
		List<Transaction> transactions = transactionRepo.findAll(combined);
		
		return transactions.stream()
					.map(this::fromTransactionToTransactionInfo)
					.collect(Collectors.toList());
	}
	
	public void deleteById(Long id) {
		transactionRepo.deleteById(id);
	}
	
	public TransactionInfo cancel(Long id) {
		Transaction transaction = transactionRepo.findById(id);
		if(transaction == null) {
			throw new TransactionNotFoundException("트랜잭션을 찾을 수 없습니다.");
		}
		if(transaction.cancel()) {
			return fromTransactionToTransactionInfo(transaction);
		}
		return null;
	}
	
	public TransactionInfo fromTransactionToTransactionInfo(Transaction transaction) {
		return new TransactionInfo(
				transaction.getMember().getId(),
				transaction.getSend().getBankcode().getCode(),
				transaction.getSend().getAccountNumber(),
				transaction.getRecv().getBankcode().getCode(),
				transaction.getRecv().getAccountNumber(),
				transaction.getAmount(),
				transaction.getAfterBalance(),
				transaction.getTransactionStatus(),
				transaction.getProcessAt(),
				transaction.getId());
	}
}
