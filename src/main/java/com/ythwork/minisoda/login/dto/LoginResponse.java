package com.ythwork.minisoda.login.dto;

import com.ythwork.minisoda.member.dto.MemberInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
	private String jwt;
	private MemberInfo memberInfo;
}
