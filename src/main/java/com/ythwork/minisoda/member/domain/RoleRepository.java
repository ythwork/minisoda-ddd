package com.ythwork.minisoda.member.domain;

import java.util.Set;

public interface RoleRepository {
	void save(Role role);
	void remove(Role role);
	Role findByRoleType(RoleType roleType);
	Set<Role> convertFromString(Set<String> roleStr);
}
