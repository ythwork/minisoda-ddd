package com.ythwork.minisoda.transaction.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ythwork.minisoda.transaction.domain.exception.TransactionNotFoundException;

@RestControllerAdvice
public class TransactionExceptionHandler {
	@ExceptionHandler(TransactionNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String transactionNotFoundHandler(TransactionNotFoundException e) {
		return e.getMessage();
	}
}
