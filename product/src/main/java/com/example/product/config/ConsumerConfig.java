package com.example.product.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.common.constants.RabbitMQConstants;
import com.example.common.rabbitmq.Consumer;
import com.example.product.consumer.ProductConsumer;

@Configuration
public class ConsumerConfig {
	@Bean
	public TopicExchange topic() {
		return new TopicExchange(RabbitMQConstants.TOPIC_PRODUCT);
	}
	@Bean
	public FanoutExchange schemaFanout() {
		return new FanoutExchange(RabbitMQConstants.FANOUT_SCHEMA);
	}
	@Bean
	public Queue queueSchema() {
		return new Queue(RabbitMQConstants.QUEUE_PRODUCT_SCHEMA);
	}	
	@Bean
	public Queue queueGetProductByAccountId() {
		return new Queue(RabbitMQConstants.QUEUE_PRODUCT_GET_ALL_BY_ACCOUNT_ID);
	}
	@Bean
	public Binding bindingSchema() {
		return BindingBuilder.bind(queueSchema()).to(schemaFanout());
	}
	@Bean 
	public Binding bindingGetProductByAccountId() {
		return BindingBuilder.bind(queueGetProductByAccountId()).to(topic()).with(RabbitMQConstants.ROUTING_PRODUCT_GET_ALL_BY_ACCOUNT_ID);
	}	
}
