package com.example.account.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.account.converter.AccountConverter;
import com.example.account.model.Account;
import com.example.account.model.UserSession;
import com.example.account.publisher.SchemaPublisher;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.UserSessionRepository;
import com.example.account.service.AccountService;
import com.example.account.service.AuthenticationService;
import com.example.account.service.SessionService;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;
import com.example.common.dto.request.LoginRequest;
import com.example.common.dto.request.RegisterRequest;
import com.example.common.dto.response.LoginResponse;
import com.example.common.dto.response.RegisterResponse;
import com.example.common.util.JwtUtils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@NoArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	private AccountService accountService;
	private SessionService sessionService;
	private AccountRepository accountRepository;
	private AuthenticationManager authenticationManager;
	private SchemaPublisher schemaPublisher;

	@Autowired
	public AuthenticationServiceImpl(AccountService accountService, SessionService sessionService,
			AccountRepository accountRepository, @Lazy AuthenticationManager authenticationManager,
			SchemaPublisher schemaPublisher) {
		super();
		this.accountService = accountService;
		this.sessionService = sessionService;
		this.accountRepository = accountRepository;
		this.authenticationManager = authenticationManager;
		this.schemaPublisher = schemaPublisher;
	}

	@Override
	public LoginResponse login(LoginRequest request) throws CustomException {
		Account account = accountRepository.findByUsernameIgnoreCase(request.getUsername())
				.orElseThrow(() -> new CustomException(APIStatus.UNAUTHORIZED, "User account does not exist"));
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isAnonymous = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS"));

		if (isAnonymous) {
			try {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			} catch (Exception e) {
				log.error("Error in login: ", e);
				throw new CustomException(APIStatus.BAD_REQUEST, "Username or password is not correct!");
			}
		}
		String token = JwtUtils.generateToken(loadUserByUsername(request.getUsername()), account.getId());
		setSession(token, account);
		return AccountConverter.convertToLoginResponse(account, token);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public RegisterResponse register(RegisterRequest request) throws CustomException {
		Account account = AccountConverter.convertFromRegisterRequest(request);
		account = accountService.create(account);
		String token = JwtUtils.generateToken(loadUserByUsername(request.getUsername()), account.getId());
		setSession(token, account);
		schemaPublisher.createNewSchema("account" + account.getId());
		return AccountConverter.convertToRegisterResponse(account, token);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String ROLE_PREFIX = "ROLE_";
		Account account = accountService.find(username);
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + account.getRole()));
		return new User(account.getUsername(), account.getPassword(), authorities);
	}

	@Override
	public void logout(String token) throws CustomException {
		sessionService.removeToken(token);
	}

	private void setSession(String token, Account acc) {
		UserSession session = new UserSession();
		session.setToken(token);
		session.setAccount(acc);
		session.setExpireAt(JwtUtils.getExpirationDateFromToken(token));
		// Delete old token if exist
		sessionService.removeOldTokens(session);
		sessionService.setSession(token, session); // Save session into RAM
	}

}
