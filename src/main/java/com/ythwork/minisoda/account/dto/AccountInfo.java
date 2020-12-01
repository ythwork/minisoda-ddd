package com.ythwork.minisoda.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountInfo {
	private String owner;
	private String bankcode;
	private String accountNumber;
	private Long balance;
	private Long accountId;
}
