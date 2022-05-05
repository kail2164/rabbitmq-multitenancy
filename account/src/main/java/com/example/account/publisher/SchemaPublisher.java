package com.example.account.publisher;


import org.springframework.stereotype.Component;

import com.example.common.constants.RabbitMQConstant;
import com.example.common.dto.CustomException;
import com.example.common.util.RabbitMQUtils;

@Component
public class SchemaPublisher {
	public void createNewSchema(String name) throws CustomException {
		RabbitMQUtils.send(RabbitMQConstant.FANOUT_SCHEMA, "", name);
	}
}
