package com.example.account.cronjob;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.example.account.model.UserSession;
import com.example.account.repository.UserSessionRepository;
import com.example.account.service.SessionService;
import com.example.common.dto.CustomException;
import com.example.common.util.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SessionCronJob implements InitializingBean {
	private ScheduledExecutorService exec;

	private SessionService sessionService;
	private UserSessionRepository userSessionRepository;

	public SessionCronJob(SessionService sessionService, UserSessionRepository userSessionRepository) {
		this.sessionService = sessionService;
		this.userSessionRepository = userSessionRepository;
	}
	
	private Runnable cleanSessionInRAM = new Runnable() {
		@Override
		public void run() {
			try {
				sessionService.checkForExpiredToken();
			} catch (CustomException e) {
				log.error("Error in: cleanSessionInRAM", e);
			}
		}
	};

	private Runnable cleanSessionInDB = new Runnable() {
		@Override
		public void run() {
			try {
				List<String> tokens = userSessionRepository.findAll().stream().map(UserSession::getToken).toList();
				List<String> expiredTokens = new ArrayList<>();
				for (String token : tokens) {
					if (JwtUtils.isTokenExpired(token)) {
						expiredTokens.add(token);
					}
				}
				sessionService.removeTokens(expiredTokens);
			} catch (CustomException e) {
				log.error("Error in: cleanSessionInDB", e);
			}
		}
	};

	@Override
	public void afterPropertiesSet() throws Exception {
		exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleWithFixedDelay(cleanSessionInRAM, 60, 30, TimeUnit.MINUTES);
		exec.scheduleWithFixedDelay(cleanSessionInDB, 1, 12, TimeUnit.HOURS);
	}



}
