package com.ythwork.minisoda.member.domain;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

public interface MemberRepository {
	void save(Member member);
	void remove(Member member);
	Member findById(Long id);
	Member findByEmail(Email email) throws NoResultException, NonUniqueResultException;
	Member findByUsername(String username) throws NoResultException, NonUniqueResultException;
	boolean exists(Member member);
}
