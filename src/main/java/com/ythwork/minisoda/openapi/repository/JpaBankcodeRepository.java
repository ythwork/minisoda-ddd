package com.ythwork.minisoda.openapi.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ythwork.minisoda.openapi.domain.Bankcode;
import com.ythwork.minisoda.openapi.domain.BankcodeRepository;

@Repository
public class JpaBankcodeRepository implements BankcodeRepository {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Bankcode findByCode(String code) {
		TypedQuery<Bankcode> query = em.createQuery("select b from Bankcode b where b.code LIKE :code", Bankcode.class);
		query.setParameter("code", code);
		return query.getSingleResult();
	}

}
