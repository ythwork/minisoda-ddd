package com.ythwork.minisoda.openapi.domain.exception;

public class OpenapiNotFoundException extends RuntimeException {
	public OpenapiNotFoundException() {}
	public OpenapiNotFoundException(String msg) {
		super(msg);
	}
	public OpenapiNotFoundException(Throwable cause) {
		initCause(cause);
	}
	
	public OpenapiNotFoundException(String msg, Throwable cause) {
		super(msg);
		initCause(cause);
	}
}
