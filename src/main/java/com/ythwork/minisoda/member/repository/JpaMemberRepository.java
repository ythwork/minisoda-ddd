package com.ythwork.minisoda.member.repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ythwork.minisoda.member.domain.Email;
import com.ythwork.minisoda.member.domain.Member;
import com.ythwork.minisoda.member.domain.MemberRepository;

@Repository
public class JpaMemberRepository implements MemberRepository {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void save(Member member) {
		if(member.getId() == null) {
			em.persist(member);
		} else {
			em.merge(member);
		}
	}

	@Override
	public void remove(Member member) {
		em.remove(member);
	}

	@Override
	public Member findById(Long id) {
		return em.find(Member.class, id);
	}
	
	@Override
	public Member findByEmail(Email email) throws NoResultException, NonUniqueResultException {
		String jpql = "select m from Member m where m.email = :email";
		TypedQuery<Member> query = em.createQuery(jpql, Member.class);
		query.setParameter("email", email);
		return query.getSingleResult();
	}

	@Override
	public boolean exists(Member member) {
		Email email = member.getEmail();
		try {
			Member get = findByEmail(email);
			return true;
		} catch(NoResultException e) {
			return false;
		}
	}

	@Override
	public Member findByUsername(String username) throws NoResultException, NonUniqueResultException {
		String jpql = "select m from Member m where m.auth.username = :username";
		TypedQuery<Member> query = em.createQuery(jpql, Member.class);
		query.setParameter("username", username);
		return query.getSingleResult();
	}

}
