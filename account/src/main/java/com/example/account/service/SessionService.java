package com.example.account.service;

import com.example.account.model.UserSession;
import com.example.common.dto.CustomException;

public interface SessionService {
	void setSession(String token, UserSession session);
	UserSession getSession(String token);
	void removeToken(String token);
	boolean isTokenExists(String token);
	void validateToken(String token) throws CustomException;	
}
