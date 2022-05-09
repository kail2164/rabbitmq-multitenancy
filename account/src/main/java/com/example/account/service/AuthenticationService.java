package com.example.account.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.common.dto.CustomException;
import com.example.common.dto.request.LoginRequest;
import com.example.common.dto.request.RegisterRequest;
import com.example.common.dto.response.LoginResponse;
import com.example.common.dto.response.RegisterResponse;

public interface AuthenticationService extends UserDetailsService {
	LoginResponse login(LoginRequest request) throws CustomException;
	RegisterResponse register(RegisterRequest request) throws CustomException;
	void logout(String token) throws CustomException;
}
