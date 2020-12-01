package com.ythwork.minisoda.member.repository;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ythwork.minisoda.member.domain.Role;
import com.ythwork.minisoda.member.domain.RoleRepository;
import com.ythwork.minisoda.member.domain.RoleType;

@Repository
public class JpaRoleRepository implements RoleRepository {
	@PersistenceContext
	private EntityManager em;
	
	@Override 
	public void save(Role role) {
		if(role.getId() == null) {
			em.persist(role);
		} else {
			em.merge(role);
		}
	}

	@Override
	public void remove(Role role) {
		em.remove(role);
	}

	@Override
	public Role findByRoleType(RoleType roleType) {
		TypedQuery<Role> query = em.createQuery("select r from Role r where r.roleType = :roleType", Role.class);
		query.setParameter("roleType", roleType);
		return query.getSingleResult();
	}

	@Override
	public Set<Role> convertFromString(Set<String> roleStr) {
		Set<Role> roles = new HashSet<>();
		if(roleStr == null) {
			Role defaultRole = findByRoleType(RoleType.ROLE_USER);
			roles.add(defaultRole);
		} else {
			roleStr.forEach(role -> {
				switch(role) {
				case "ADMIN":
					Role adminRole = findByRoleType(RoleType.ROLE_ADMIN);
					roles.add(adminRole);
					break;
				default:
					Role userRole = findByRoleType(RoleType.ROLE_USER);
					roles.add(userRole);
				}
			});
		}
		return roles;
	}
}
