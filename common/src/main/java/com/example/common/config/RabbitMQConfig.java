package com.example.common.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class RabbitMQConfig {
	@Autowired
	SimpleRabbitListenerContainerFactory simpleContainer;

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		final var rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
		simpleContainer.setDefaultRequeueRejected(false);
		return rabbitTemplate;
	}

	@SuppressWarnings("deprecation")
	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// This item must be configured, otherwise it will report
		// java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		return new Jackson2JsonMessageConverter(objectMapper);
	}
}
