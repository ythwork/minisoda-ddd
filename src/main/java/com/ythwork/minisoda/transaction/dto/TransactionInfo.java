package com.ythwork.minisoda.transaction.dto;

import java.util.Date;

import com.ythwork.minisoda.transaction.domain.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionInfo {
	private Long memberId;
	private String sendCode;
	private String sendAcntNum;
	private String recvCode;
	private String recvAcntNum;
	private Long amount;
	private Long afterBalance;
	private TransactionStatus transactionStatus;
	private Date processAt;
	private Long transactionId;
}
