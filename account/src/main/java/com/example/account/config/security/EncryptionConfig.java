package com.example.account.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.account.config.encoder.CustomPasswordEncoder;
import com.example.account.service.AuthenticationService;

@Configuration
public class EncryptionConfig {

	@Autowired
	private AuthenticationService authenService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenService).passwordEncoder(passwordEncoder);
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new CustomPasswordEncoder();
	}
}
