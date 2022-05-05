package com.example.product.consumer;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.product.service.SchemaService;

@Component
public class SchemaConsumer {
	@Autowired
	Queue queueSchema;
	@Autowired
	SchemaService schemaService;
	
	@RabbitListener(queues = "#{queueSchema.name}")
	public void receivedSchema(String schemaName) {
		schemaService.createSchema(schemaName);		
	}
}
