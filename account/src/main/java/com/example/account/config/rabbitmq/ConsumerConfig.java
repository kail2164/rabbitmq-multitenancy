package com.example.account.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.account.consumer.AccountConsumer;
import com.example.account.consumer.SessionConsumer;
import com.example.common.constants.RabbitMQConstants;
import com.example.common.rabbitmq.Consumer;

@Configuration
public class ConsumerConfig {
	@Bean
	public TopicExchange topic() {
		return new TopicExchange(RabbitMQConstants.TOPIC_ACCOUNT);
	}
	@Bean
	public Queue queueLogout() {
		return new Queue(RabbitMQConstants.QUEUE_ACCOUNT_LOGOUT);
	}
	@Bean
	public Queue queueValidateToken() {
		return new Queue(RabbitMQConstants.QUEUE_ACCOUNT_VALIDATE_TOKEN);
	}
	@Bean
	public Queue queueFetchAllSchemas() {
		return new Queue(RabbitMQConstants.QUEUE_ACCOUNT_GET_ALL_SCHEMAS);
	}
	@Bean
	public Queue queueGetUserDetails() {
		return new Queue(RabbitMQConstants.QUEUE_ACCOUNT_GET_USER_DETAILS);
	}
	@Bean 
	public Binding bindingLogout() {
		return BindingBuilder.bind(queueLogout()).to(topic()).with(RabbitMQConstants.ROUTING_ACCOUNT_LOGOUT);
	}
	@Bean 
	public Binding bindingValidateToken() {
		return BindingBuilder.bind(queueValidateToken()).to(topic()).with(RabbitMQConstants.ROUTING_ACCOUNT_VALIDATE_TOKEN);
	}
	@Bean 
	public Binding bindingFetchAllSchemas() {
		return BindingBuilder.bind(queueFetchAllSchemas()).to(topic()).with(RabbitMQConstants.ROUTING_ACCOUNT_GET_ALL_SCHEMAS);
	}
	@Bean 
	public Binding bindingGetUserDetails() {
		return BindingBuilder.bind(queueGetUserDetails()).to(topic()).with(RabbitMQConstants.ROUTING_ACCOUNT_GET_USER_DETAILS);
	}	
}
