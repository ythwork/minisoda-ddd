package com.ythwork.minisoda.member.domain.exception;

public class MemberAlreadyExistsException extends RuntimeException {
	public MemberAlreadyExistsException() {}
	public MemberAlreadyExistsException(String msg) {
		super(msg);
	}
	public MemberAlreadyExistsException(Throwable cause) {
		initCause(cause);
	}
	
	public MemberAlreadyExistsException(String msg, Throwable cause) {
		super(msg);
		initCause(cause);
	}
}
