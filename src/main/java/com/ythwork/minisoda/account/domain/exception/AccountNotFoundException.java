package com.ythwork.minisoda.account.domain.exception;

public class AccountNotFoundException extends RuntimeException {
	public AccountNotFoundException() {}
	public AccountNotFoundException(String msg) {
		super(msg);
	}
	public AccountNotFoundException(Throwable cause) {
		initCause(cause);
	}
	
	public AccountNotFoundException(String msg, Throwable cause) {
		super(msg);
		initCause(cause);
	}
}
