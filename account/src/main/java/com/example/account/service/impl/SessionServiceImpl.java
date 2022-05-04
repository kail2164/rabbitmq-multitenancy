package com.example.account.service.impl;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.account.model.UserSession;
import com.example.account.repository.UserSessionRepository;
import com.example.account.service.SessionService;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;

@Service
public class SessionServiceImpl implements SessionService {
	@Autowired
	UserSessionRepository userSessionRepository;

	private static HashMap<String, UserSession> cacheHashMap = new HashMap<>();

	@Override
	public void setSession(String token, UserSession session) {
		cacheHashMap.put(token, session);
	}

	@Override
	public UserSession getSession(String token) {		
		return cacheHashMap.get(token);
	}

	@Override
	public void removeToken(String token) {
		cacheHashMap.remove(token);
		deleteTokenIfExist(token);
	}

	@Override
	public boolean isTokenExists(String token) {		
		return cacheHashMap.containsKey(token);
	}

	@Override
	public void validateToken(String token) throws CustomException {
		if(!isTokenExists(token)){
			userSessionRepository.findById(token).orElseThrow(()-> new CustomException(APIStatus.BAD_REQUEST, "Invalid token"));
		}
	}
	private void deleteTokenIfExist(String token) {
		userSessionRepository.findById(token).ifPresent(session -> userSessionRepository.deleteById(token));		
	}
}
