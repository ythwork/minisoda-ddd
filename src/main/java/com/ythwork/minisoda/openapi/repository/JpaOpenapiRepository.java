package com.ythwork.minisoda.openapi.repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ythwork.minisoda.account.domain.Account;
import com.ythwork.minisoda.openapi.domain.Openapi;
import com.ythwork.minisoda.openapi.domain.OpenapiRepository;

@Repository
public class JpaOpenapiRepository implements OpenapiRepository {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Openapi findByAccount(Account account) throws NoResultException, NonUniqueResultException {
		String jpql = "select o from Account a inner join a.openapi o where a.id = :id";
		TypedQuery<Openapi> query = em.createQuery(jpql, Openapi.class);
		query.setParameter("id", account.getId());
		
		return query.getSingleResult();
	}
	
	@Override
	public Openapi findByBankcodeAndAccountNumber(String code, String accountNumber) throws NoResultException, NonUniqueResultException {
		String jpqlQuery = "select o from Openapi o where o.bankcode.code like :code and o.accountNumber like :accountNumber";
		
		TypedQuery<Openapi> query = em.createQuery(jpqlQuery, Openapi.class);
		query.setParameter("code", code);
		query.setParameter("accountNumber", accountNumber);
		
		return query.getSingleResult();
	}

	@Override
	public Openapi findById(Long id) {
		TypedQuery<Openapi> query = em.createQuery("select o from Openapi o where o.id = :id", Openapi.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}

}
