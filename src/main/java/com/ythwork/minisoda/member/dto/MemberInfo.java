package com.ythwork.minisoda.member.dto;

import com.ythwork.minisoda.member.domain.Address;
import com.ythwork.minisoda.member.domain.Email;
import com.ythwork.minisoda.member.domain.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfo {
	private String fullName;
	private Address address;
	private PhoneNumber phoneNumber;
	private Email email;
	private Long memberId;
}
