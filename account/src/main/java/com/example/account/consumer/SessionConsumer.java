package com.example.account.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.stereotype.Component;

import com.example.account.service.AuthenticationService;
import com.example.account.service.SessionService;
import com.example.common.rabbitmq.Consumer;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class SessionConsumer implements Consumer{
	private SessionService sessionService;
	private AuthenticationService authenticationService;
	
	@Autowired
	public SessionConsumer(
			SessionService sessionService,
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
		this.sessionService = sessionService;
		
	}
	
	@RabbitListener(queues = "#{queueValidateToken.name}", returnExceptions = "true")
	public RemoteInvocationResult validateToken(String token) throws Exception {
		sessionService.validateToken(token);
		return new RemoteInvocationResult(true);
	}
	
	@RabbitListener(queues = "#{queueLogout.name}", returnExceptions = "true")
	public RemoteInvocationResult logout(String token) throws Exception {
		authenticationService.logout(token);
		return new RemoteInvocationResult(true);
	}
}
