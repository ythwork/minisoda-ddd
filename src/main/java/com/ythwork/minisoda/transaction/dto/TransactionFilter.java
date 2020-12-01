package com.ythwork.minisoda.transaction.dto;

import com.ythwork.minisoda.transaction.domain.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFilter {
	private Long memberId;
	private Long sendAcntId;
	private String from;
	private String to;
	private TransactionStatus status;
	private Long amount;
}
