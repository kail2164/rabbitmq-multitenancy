package com.example.account.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.account.consumer.AccountConsumer;
import com.example.account.consumer.SessionConsumer;
import com.example.common.constants.RabbitMQConstant;
import com.example.common.rabbitmq.Consumer;

@Configuration
public class ConsumerConfig {
	@Bean
	public TopicExchange topic() {
		return new TopicExchange(RabbitMQConstant.TOPIC_ACCOUNT);
	}
	@Bean
	public Queue queueLogout() {
		return new Queue(RabbitMQConstant.QUEUE_ACCOUNT_LOGOUT);
	}
	@Bean
	public Queue queueValidateToken() {
		return new Queue(RabbitMQConstant.QUEUE_ACCOUNT_VALIDATE_TOKEN);
	}
	@Bean
	public Queue queueFetchAllSchemas() {
		return new Queue(RabbitMQConstant.QUEUE_ACCOUNT_GET_ALL_SCHEMAS);
	}
	@Bean 
	public Binding bindingLogout() {
		return BindingBuilder.bind(queueLogout()).to(topic()).with(RabbitMQConstant.ROUTING_ACCOUNT_LOGOUT);
	}
	@Bean 
	public Binding bindingValidateToken() {
		return BindingBuilder.bind(queueValidateToken()).to(topic()).with(RabbitMQConstant.ROUTING_ACCOUNT_VALIDATE_TOKEN);
	}
	@Bean 
	public Binding bindingFetchAllSchemas() {
		return BindingBuilder.bind(queueFetchAllSchemas()).to(topic()).with(RabbitMQConstant.ROUTING_ACCOUNT_GET_ALL_SCHEMAS);
	}	
}
