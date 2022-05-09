package com.example.account.consumer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.stereotype.Component;

import com.example.account.repository.AccountRepository;
import com.example.common.rabbitmq.Consumer;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class AccountConsumer implements Consumer {
	private AccountRepository accountRepository;
	
	@Autowired
	public AccountConsumer(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@RabbitListener(queues = "#{queueFetchAllSchemas.name}", returnExceptions = "true")
	public RemoteInvocationResult queueFetchAllSchemas() throws Exception {
		List<String> result = accountRepository.getAllAccountId()
				.stream()
				.map(data -> "account" + data)
				.collect(Collectors.toList());
		return new RemoteInvocationResult(result);
	}
}
