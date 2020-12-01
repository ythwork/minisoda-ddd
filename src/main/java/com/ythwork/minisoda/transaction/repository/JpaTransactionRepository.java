package com.ythwork.minisoda.transaction.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.transaction.domain.Transaction;
import com.ythwork.minisoda.transaction.domain.TransactionRepository;

@Repository
public class JpaTransactionRepository implements TransactionRepository {
	@PersistenceContext
	private EntityManager em;
	@Override
	public void save(Transaction transaction) {
		if(transaction.getId() == null) {
			em.persist(transaction);
		} else {
			em.merge(transaction);
		}
	}

	@Override
	public void remove(Transaction transaction) {
		em.remove(transaction);

	}

	@Override
	public Transaction findById(Long id) {
		return em.find(Transaction.class, id);
	}

	@Override
	public List<Transaction> findByMember(Member member) {
		String jpql = "select t from Transaction t where t.member = :member";
		TypedQuery<Transaction> query = em.createQuery(jpql, Transaction.class);
		query.setParameter("member", member);
		return query.getResultList();
	}

	@Override
	public List<Transaction> findAll(Specification<Transaction> spec) {
		// builder와 query를 만들고
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
		// aggregate root
		Root<Transaction> root = cq.from(Transaction.class);
		// predicate를 구하고
		Predicate predicate = spec.toPredicate(root, cq, cb);
		// where 절을 만들고
		cq.where(predicate);
		// 실제  query를 만들어서 
		TypedQuery<Transaction> query = em.createQuery(cq);
		
		return query.getResultList();
	}

	@Override
	public void deleteById(Long id) {
		em.remove(findById(id));
	}
	
}
