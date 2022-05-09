package com.example.account.service;

import com.example.account.model.UserSession;
import com.example.common.dto.CustomException;

public interface SessionService {
	void setSession(String token, UserSession session) throws CustomException;	
	UserSession getSession(String token);
	void removeToken(String token) throws CustomException;
	void removeOldTokens(UserSession session) throws CustomException;	
	boolean isTokenExists(String token);
	void validateToken(String token) throws CustomException;	
}
