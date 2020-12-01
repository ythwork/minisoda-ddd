package com.ythwork.minisoda.openapi.domain;

public interface BankcodeRepository {
	Bankcode findByCode(String code);
}
