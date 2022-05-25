package com.example.product.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.example.product.service.SchemaService;

import lombok.NoArgsConstructor;

@Component
@DependsOn("rabbitMQConfig")
public class SchemaConsumer {
	private SchemaService schemaService;

	@Autowired
	public SchemaConsumer(SchemaService schemaService) {
		this.schemaService = schemaService;
	}
	
	@RabbitListener(queues = "#{queueSchema.name}")
	public void receivedSchema(String schemaName) {
		schemaService.createSchema(schemaName);
	}

	
}
