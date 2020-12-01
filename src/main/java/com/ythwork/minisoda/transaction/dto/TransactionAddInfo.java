package com.ythwork.minisoda.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAddInfo {
	private Long memberId;
	private Long sendAcntId;
	private String recvCode;
	private String recvAcntNum;
	private Long amount;
}
