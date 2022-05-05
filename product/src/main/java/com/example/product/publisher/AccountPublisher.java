package com.example.product.publisher;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.common.constants.RabbitMQConstant;
import com.example.common.dto.CustomException;
import com.example.common.util.RabbitMQUtils;

@Component
public class AccountPublisher {
	public List<String> fetchAllSchemas() throws CustomException {
		return RabbitMQUtils.sendAndReceiveList(RabbitMQConstant.TOPIC_ACCOUNT, RabbitMQConstant.ROUTING_ACCOUNT_GET_ALL_SCHEMAS,
				"", String.class);
	}
}
