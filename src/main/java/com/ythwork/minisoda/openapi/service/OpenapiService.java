package com.ythwork.minisoda.openapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ythwork.minisoda.openapi.domain.Openapi;
import com.ythwork.minisoda.openapi.domain.OpenapiRepository;
import com.ythwork.minisoda.openapi.domain.exception.OpenapiNotFoundException;

@Service
@Transactional
public class OpenapiService {
	private OpenapiRepository openapiRepo;
	
	public OpenapiService(OpenapiRepository openapiRepo) {
		this.openapiRepo = openapiRepo;
	}
	
	public Openapi findOpenapi(String code, String accountNumber) {
		Openapi api = openapiRepo.findByBankcodeAndAccountNumber(code, accountNumber);
		if(api == null) {
			throw new OpenapiNotFoundException("계좌가 존재하지 않습니다.");
		} 
		return api;
	}
	
	public Openapi findById(Long id) {
		Openapi api = openapiRepo.findById(id);
		if(api == null) {
			throw new OpenapiNotFoundException("계좌가 존재하지 않습니다.");
		}
		return api;
	}
}
