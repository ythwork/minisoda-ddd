package com.ythwork.minisoda.openapi.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ythwork.minisoda.openapi.domain.exception.OpenapiNotFoundException;

@RestControllerAdvice
public class OpenapiExceptionAdvice {
	@ExceptionHandler(OpenapiNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String notFoundOpenapiHandler(OpenapiNotFoundException e) {
		return e.getMessage();
	}
}
