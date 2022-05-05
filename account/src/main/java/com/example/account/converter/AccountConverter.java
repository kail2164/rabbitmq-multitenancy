package com.example.account.converter;

import com.example.account.model.Account;
import com.example.common.dto.request.LoginRequest;
import com.example.common.dto.request.RegisterRequest;
import com.example.common.dto.response.LoginResponse;
import com.example.common.dto.response.RegisterResponse;

public class AccountConverter {
	public static Account convertFromRegisterRequest(RegisterRequest request) {
		Account acc = new Account();
		acc.setFirstName(request.getFirstName());
		acc.setLastName(request.getLastName());
		acc.setPassword(request.getPassword());
		acc.setUsername(request.getUsername());
		acc.setRole("user");
		return acc;
	}
	public static RegisterResponse convertToRegisterResponse(Account acc, String token) {
		RegisterResponse response = new RegisterResponse();
		response.setId(acc.getId());
		response.setFirstName(acc.getFirstName());
		response.setLastName(acc.getLastName());
		response.setRole(acc.getRole());
		response.setUsername(acc.getUsername());
		response.setToken(token);
		return response;
	}
	
	public static Account convertFromLoginRequest(LoginRequest request) {
		Account acc = new Account();
		acc.setUsername(request.getUsername());
		acc.setPassword(request.getPassword());
		return acc;
	}
	
	public static LoginResponse convertToLoginResponse(Account acc, String token) {
		LoginResponse response = new LoginResponse();
		response.setId(acc.getId());
		response.setFirstName(acc.getFirstName());
		response.setLastName(acc.getLastName());
		response.setRole(acc.getRole());
		response.setToken(token);
		response.setUsername(acc.getUsername());
		return response;
	}
}
