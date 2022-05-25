package com.example.account.publisher;


import org.springframework.stereotype.Component;

import com.example.common.constants.RabbitMQConstants;
import com.example.common.dto.CustomException;
import com.example.common.util.RabbitMQUtils;

import lombok.NoArgsConstructor;

@Component
public class SchemaPublisher {
private RabbitMQUtils rabbitMQUtils;
	
	public SchemaPublisher(RabbitMQUtils rabbitMQUtils) {
		this.rabbitMQUtils = rabbitMQUtils;
	}

	public void createNewSchema(String name) throws CustomException {
		rabbitMQUtils.send(RabbitMQConstants.FANOUT_SCHEMA, "", name);
	}
}
