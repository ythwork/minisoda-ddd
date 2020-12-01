package com.ythwork.minisoda.account.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ythwork.minisoda.account.domain.Account;
import com.ythwork.minisoda.account.domain.AccountRepository;
import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.openapi.domain.Openapi;

@Repository
public class JpaAccountRepository implements AccountRepository {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void save(Account account) {
		if(account.getId() == null) {
			em.persist(account);
		} else {
			em.merge(account);
		}
	}

	@Override
	public Account findById(Long id) {
		return em.find(Account.class, id);
	}

	@Override
	public void remove(Account account) {
		em.remove(account);
	}

	@Override
	public List<Account> findByMember(Member member) {
		String jpql = "select a from Account a where a.member = :member";
		TypedQuery<Account> query = em.createQuery(jpql, Account.class);
		query.setParameter("member", member);
		return query.getResultList();
	}
	
	@Override
	public Account findByOpenapi(Openapi openapi) {
		String jpql = "select a Account a where a.openapi = :openapi";
		TypedQuery<Account> query = em.createQuery(jpql, Account.class);
		query.setParameter("openapi", openapi);
		return query.getSingleResult();
	}
}
