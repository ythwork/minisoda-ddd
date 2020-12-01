package com.ythwork.minisoda.member.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ythwork.minisoda.member.domain.exception.MemberAlreadyExistsException;
import com.ythwork.minisoda.member.domain.exception.MemberNotFoundException;

@RestControllerAdvice
public class MemberExceptionAdvice {
	@ExceptionHandler(MemberAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String memberAlreadyExistsHandler(MemberAlreadyExistsException e) {
		return e.getMessage();
	}
	
	@ExceptionHandler(MemberNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String memberNotFoundHandler(MemberNotFoundException e) {
		return e.getMessage();
	}
	
}
