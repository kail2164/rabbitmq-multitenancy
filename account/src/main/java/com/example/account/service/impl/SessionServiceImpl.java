package com.example.account.service.impl;

import java.util.HashMap;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.account.model.UserSession;
import com.example.account.repository.UserSessionRepository;
import com.example.account.service.SessionService;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@NoArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {	
	private UserSessionRepository userSessionRepository;

	@Autowired
	public SessionServiceImpl(UserSessionRepository userSessionRepository) {
		super();
		this.userSessionRepository = userSessionRepository;
	}
	
	private static HashMap<String, UserSession> cacheHashMap = new HashMap<>();

	@Override
	public void setSession(String token, UserSession session) throws CustomException{
		if(session == null) {
			throw new CustomException(APIStatus.BAD_REQUEST, "User session cannot be null");
		}
		if(token == null) {
			throw new CustomException(APIStatus.BAD_REQUEST, "Token cannot be null");
		}
		cacheHashMap.put(token, session);
		userSessionRepository.save(session);
	}

	@Override
	public UserSession getSession(String token) {		
		return cacheHashMap.get(token);
	}

	@Override
	public void removeOldTokens(UserSession session) throws CustomException{
		if(session == null) {
			throw new CustomException(APIStatus.BAD_REQUEST, "User session cannot be null");
		}
		for(Entry<String, UserSession> entry : cacheHashMap.entrySet()) {
			if(session.equals(entry.getValue())) {
				removeToken(entry.getKey());
			}
		}		
	}
	
	@Override
	public void removeToken(String token) throws CustomException{
		if(token == null) {
			throw new CustomException(APIStatus.BAD_REQUEST, "Token cannot be null");
		}
		cacheHashMap.remove(token);
		deleteOldToken(token);
	}

	@Override
	public boolean isTokenExists(String token) {		
		return cacheHashMap.containsKey(token);
	}

	@Override
	public void validateToken(String token) throws CustomException {
		log.error(token);
		if(!isTokenExists(token)){
			log.error("finding in DB");
			userSessionRepository.findById(token).orElseThrow(()-> new CustomException(APIStatus.BAD_REQUEST, "Invalid token"));
		}
	}
	private void deleteOldToken(String token) {
		userSessionRepository.findById(token).ifPresent(session -> userSessionRepository.deleteById(token));		
	}

	
}
