package com.ythwork.minisoda.security.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ythwork.minisoda.security.exception.NotAllowedMemberException;

@RestControllerAdvice
public class SecurityExceptionAdvice {
	@ExceptionHandler(NotAllowedMemberException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	String notAllowedMemberHandler(NotAllowedMemberException e) {
		return e.getMessage();
	}
}
