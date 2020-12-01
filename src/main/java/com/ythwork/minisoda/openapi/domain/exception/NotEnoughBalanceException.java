package com.ythwork.minisoda.openapi.domain.exception;

// rollback이 가능하도록 런타임 익셉션으로 만든다.
public class NotEnoughBalanceException extends RuntimeException {
	public NotEnoughBalanceException(String msg) {
		super(msg);
	}
}
