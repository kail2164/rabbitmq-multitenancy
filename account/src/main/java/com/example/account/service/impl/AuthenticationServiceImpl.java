package com.example.account.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.account.converter.AccountConverter;
import com.example.account.model.Account;
import com.example.account.model.UserSession;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.UserSessionRepository;
import com.example.account.service.AccountService;
import com.example.account.service.AuthenticationService;
import com.example.account.service.SessionService;
import com.example.account.util.JwtUtil;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;
import com.example.common.dto.request.LoginRequest;
import com.example.common.dto.request.RegisterRequest;
import com.example.common.dto.response.LoginResponse;
import com.example.common.dto.response.RegisterResponse;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	@Autowired
	AccountService accountService;
	@Autowired
	SessionService sessionService;
	
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	UserSessionRepository userSessionRepository;
	
	@Autowired
	@Lazy
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtil jwtUtil;
	
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
				throw new CustomException(APIStatus.BAD_REQUEST,
						"Username or password is not correct. If you forgot your password, click below to reset.");
			}
		}
		String token = jwtUtil.generateToken(loadUserByUsername(request.getUsername()), account.getId());
		// Delete old token if exist
		sessionService.removeToken(token);
		UserSession session = new UserSession();
		session.setToken(token);
		session.setAccount(account);
		session.setExpireAt(jwtUtil.getExpirationDateFromToken(token));
		userSessionRepository.save(session); // save session to DB
		sessionService.setSession(token, session); // Save session into RAM
		return AccountConverter.convertToLoginResponse(account, token);
	}

	@Override
	public RegisterResponse register(RegisterRequest request) throws CustomException {
		Account account = AccountConverter.convertFromRegisterRequest(request);		
		account = accountService.create(account);		
		String token = jwtUtil.generateToken(loadUserByUsername(request.getUsername()), account.getId());
		return AccountConverter.convertToRegisterResponse(account, token);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountService.find(username);
		return new User(account.getUsername(), account.getPassword(), new ArrayList<>());
	}

	@Override
	public void logout(String token) throws CustomException {
		sessionService.removeToken(token);
	}

}
