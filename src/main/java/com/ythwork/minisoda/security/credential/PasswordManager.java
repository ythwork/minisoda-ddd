package com.ythwork.minisoda.security.credential;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordManager {
	private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	public static PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}
}
