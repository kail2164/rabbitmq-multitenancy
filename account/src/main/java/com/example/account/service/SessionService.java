package com.example.account.service;

import java.util.List;

import com.example.account.model.UserSession;
import com.example.common.dto.CustomException;

public interface SessionService {
	void setSession(String token, UserSession session) throws CustomException;	
	UserSession getSession(String token);
	void removeTokens(List<String> tokens) throws CustomException;
	void removeOldTokens(UserSession session) throws CustomException;	
	boolean isTokenExists(String token);
	void validateToken(String token) throws CustomException;	
	void checkForExpiredToken() throws CustomException;
}
